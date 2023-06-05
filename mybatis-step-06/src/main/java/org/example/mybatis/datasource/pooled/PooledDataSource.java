package org.example.mybatis.datasource.pooled;

import org.example.mybatis.datasource.unpooled.UnPooledDataSource;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.*;
import java.util.logging.Logger;

public class PooledDataSource implements DataSource {

    private org.slf4j.Logger logger = LoggerFactory.getLogger(PooledConnection.class);

    private final PooledState state = new PooledState(this);

    private final UnPooledDataSource dataSource;

    protected int poolMaximumActiveConnections = 10;

    protected int poolMaximumIdleConnections = 5;

    protected int poolMinimumCheckoutTime = 20000;

    protected int poolTimeToWait = 2000;

    protected String poolPingQuery = "NO PING QUERY SET";

    protected boolean poolPingEnabled = false;

    protected int poolPingConnectionsNotUsedForPing = 0;

    private int expectedConnectionCountTypeCode;

    public PooledDataSource() {
        dataSource = new UnPooledDataSource();
    }


    public void pushConnection(PooledConnection connection) throws SQLException {
        synchronized (state) {
            state.activeConnections.remove(connection);
            if (connection.isValid()){
                if (state.idleConnections.size() < poolMaximumIdleConnections && connection.getConnectionTypeCode() == expectedConnectionCountTypeCode){
                    state.accumulatedCheckoutTime += connection.getCheckoutTime();
                    if (!connection.getRealConnection().getAutoCommit()){
                        connection.getRealConnection().rollback();
                    }

                    PooledConnection newConnection = new PooledConnection(connection.getRealConnection(), this);
                    state.idleConnections.add(newConnection);
                    newConnection.setCreatedTimestamp(connection.getCreatedTimestamp());
                    newConnection.setLastUsedTimestamp(connection.getLastUsedTimestamp());
                    connection.invalidate();
                    logger.info("Returned connection " + newConnection.getRealConnection() + " to pool.");

                    state.notifyAll();
                }
                else {
                    state.accumulatedCheckoutTime += connection.getCheckoutTime();
                    if (! connection.getRealConnection().getAutoCommit()){
                        connection.getRealConnection().rollback();
                    }
                    connection.getRealConnection().close();
                    logger.info("Closed connection " + connection.getRealHashCode() + ".");
                    connection.invalidate();
                }
            }else {
                logger.info("A bad connection (" + connection.getRealHashCode() + ") attempted to return the pool, discarding connection");
                state.badConnectCount++;
            }
        }
    }

    private PooledConnection popConnection(String username, String password) throws SQLException{
        boolean countedWait = false;
        PooledConnection conn = null;
        long t = System.currentTimeMillis();
        int localBadConnectCount = 0;

        while (conn == null){
            synchronized (state){
                if (!state.idleConnections.isEmpty()){
                    conn = state.idleConnections.remove(0);
                    logger.info("checked out connection " + conn.getRealHashCode() + " from pool.");
                }
                else{
                    if (state.activeConnections.size() < poolMaximumActiveConnections){
                        conn = new PooledConnection(dataSource.getConnection(), this);
                        logger.info("Create connection " + conn.getRealHashCode() + ".");
                    }
                    else {
                        PooledConnection oldestActiveConnection = state.activeConnections.get(0);
                        long longestCheckoutTime = oldestActiveConnection.getCheckoutTime();
                        if (longestCheckoutTime > poolMinimumCheckoutTime){
                            state.claimedOverdueConnectionCount++;
                            state.accumulatedCheckoutTimeOfOverdueConnections += longestCheckoutTime;
                            state.accumulatedCheckoutTime += longestCheckoutTime;
                            state.activeConnections.remove(oldestActiveConnection);
                            if (! oldestActiveConnection.getRealConnection().getAutoCommit()){
                                oldestActiveConnection.getRealConnection().rollback();
                            }

                            conn = new PooledConnection(oldestActiveConnection.getRealConnection(), this);
                            oldestActiveConnection.invalidate();
                            logger.info("claimed overdue connection " + conn.getRealHashCode() + ".");
                        }
                        else {
                            try {
                                if (!countedWait){
                                    state.hadToWaitTime++;
                                    countedWait = true;
                                }
                                logger.info("Waiting as long as " + poolTimeToWait + " milliseconds for connection.");
                                long wt = System.currentTimeMillis();
                                state.wait(poolTimeToWait);
                                state.accumulatedWaitTime += System.currentTimeMillis() - wt;
                            } catch (InterruptedException e) {
                                break;
                            }
                        }
                    }
                }

                if (conn != null) {
                    if (conn.isValid()){
                        if (!conn.getRealConnection().getAutoCommit()){
                            conn.getRealConnection().rollback();
                        }
                        conn.setConnectionTypeCode(assembleConnectionTypeCode(dataSource.getUrl(), username, password));

                        conn.setCheckoutTimestamp(System.currentTimeMillis());
                        conn.setLastUsedTimestamp(System.currentTimeMillis());
                        state.activeConnections.add(conn);
                        state.requestCount++;
                        state.accumulatedRequestTime += System.currentTimeMillis() - t;
                    }else {
                        logger.info("A bad connection (" + conn.getRealHashCode() + ") wa returned from the pool, getting another connection.");
                        state.badConnectCount++;
                        localBadConnectCount++;
                        conn = null;

                        if (localBadConnectCount > (poolMaximumIdleConnections + 3)){
                            logger.debug("PooledDataSource: could not get a good connection to the database.");
                            throw new SQLException("PooledDataSource: could not get a good connection to the database.");
                        }
                    }
                }
            }
        }
        if (conn == null) {
            logger.debug("PooledDataSource: unknown severe error condition. The connection pool returned  a null connection.");
            throw new SQLException("PooledDataSource: unknown severe error condition. The connection pool returned  a null connection.");
        }

        return conn;

    }

    private int assembleConnectionTypeCode(String url, String username, String password) {
        return ("" + url + username + password).hashCode();
    }


    public void forceCloseAllConnections(){
        synchronized (state){
            expectedConnectionCountTypeCode = assembleConnectionTypeCode(dataSource.getUrl(), dataSource.getUsername(), dataSource.getPassword());
            for (int i = 0; i < state.activeConnections.size(); i++) {
                try {
                    PooledConnection conn = state.activeConnections.remove(i - 1);
                    conn.invalidate();

                    Connection realConn = conn.getRealConnection();
                    if (! realConn.getAutoCommit()){
                        realConn.rollback();
                    }
                    realConn.close();
                } catch (SQLException e) {
                }
            }

            for (int i = 0; i < state.idleConnections.size(); i++) {
                try {
                    PooledConnection conn = state.idleConnections.remove(i - 1);
                    conn.invalidate();

                    Connection realConn = conn.getRealConnection();
                    if (! realConn.getAutoCommit()){
                        realConn.rollback();
                    }
                } catch (SQLException e) {

                }
            }
            logger.info("PooledDataSource forcefully closed/removed all connections");
        }
    }



    protected boolean pingConnection(PooledConnection conn){
        boolean result = true;

        try {
            result = ! conn.getRealConnection().isClosed();
        } catch (SQLException e) {
            logger.info("connection " + conn.getRealConnection() + " is BAD: " + e.getMessage());
            result = false;
        }

        if (result){
            if (poolPingEnabled){
                if (poolPingConnectionsNotUsedForPing >= 0 && conn.getTimeElapsedSinceLastUse() > poolPingConnectionsNotUsedForPing){
                    try {
                        logger.info("Testing connection " + conn.getRealHashCode() + " ...");
                        Connection realConn = conn.getRealConnection();
                        Statement statement = realConn.createStatement();
                        ResultSet resultSet = statement.executeQuery(poolPingQuery);
                        resultSet.close();
                        if (! realConn.getAutoCommit()){
                            realConn.rollback();
                        }
                        result  = true;
                        logger.info("connection " + conn.getRealHashCode() + "is GOOD!");
                    }catch (Exception e){
                        logger.info("Execution of ping query '" + poolPingQuery + "' failed: " + e.getMessage());
                        try {
                            conn.getRealConnection().close();
                        } catch (SQLException ex) {
                        }
                        result = false;
                        logger.info("Connection " + conn.getRealHashCode() + " is Bad: " + e.getMessage());
                    }
                }
            }
        }
        return result;
    }

    public static Connection unwrapConnection(Connection conn){
        if (Proxy.isProxyClass(conn.getClass())){
            InvocationHandler handler = Proxy.getInvocationHandler(conn);
            if (handler instanceof PooledConnection){
                return ((PooledConnection) handler).getRealConnection();
            }
        }
        return conn;
    }


    @Override
    public Connection getConnection() throws SQLException {
        return popConnection(dataSource.getUsername(), dataSource.getPassword()).getProxyConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return popConnection(username, password).getProxyConnection();
    }

    protected void finalize() throws Throwable{
        forceCloseAllConnections();
        super.finalize();
    }


    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return DriverManager.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        DriverManager.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        DriverManager.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return DriverManager.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw  new SQLException(getClass().getName() + "is not a wrapper.");
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }


    public void setSetDriverClassName(String driver) {
        dataSource.setDriverClassName(driver);
        forceCloseAllConnections();
    }

    public void setUrl(String url) {
        dataSource.setUrl(url);
        forceCloseAllConnections();
    }

    public void setUsername(String username) {
        dataSource.setUsername(username);
        forceCloseAllConnections();
    }

    public void setPassword(String password) {
        dataSource.setPassword(password);
        forceCloseAllConnections();
    }

    public void setDefaultAutoCommit(boolean defaultAutoCommit) {
        dataSource.setAutoCommit(defaultAutoCommit);
        forceCloseAllConnections();
    }


    public int getPoolMaximumActiveConnections() {
        return poolMaximumActiveConnections;
    }

    public void setPoolMaximumActiveConnections(int poolMaximumActiveConnections) {
        this.poolMaximumActiveConnections = poolMaximumActiveConnections;
    }

    public int getPoolMaximumIdleConnections() {
        return poolMaximumIdleConnections;
    }

    public void setPoolMaximumIdleConnections(int poolMaximumIdleConnections) {
        this.poolMaximumIdleConnections = poolMaximumIdleConnections;
    }

    public int getPoolMinimumCheckoutTime() {
        return poolMinimumCheckoutTime;
    }

    public void setPoolMinimumCheckoutTime(int poolMinimumCheckoutTime) {
        this.poolMinimumCheckoutTime = poolMinimumCheckoutTime;
    }

    public int getPoolTimeToWait() {
        return poolTimeToWait;
    }

    public void setPoolTimeToWait(int poolTimeToWait) {
        this.poolTimeToWait = poolTimeToWait;
    }

    public String getPoolPingQuery() {
        return poolPingQuery;
    }

    public void setPoolPingQuery(String poolPingQuery) {
        this.poolPingQuery = poolPingQuery;
    }

    public boolean isPoolPingEnabled() {
        return poolPingEnabled;
    }

    public void setPoolPingEnabled(boolean poolPingEnabled) {
        this.poolPingEnabled = poolPingEnabled;
    }

    public int getPoolPingConnectionsNotUsedForPing() {
        return poolPingConnectionsNotUsedForPing;
    }

    public void setPoolPingConnectionsNotUsedForPing(int poolPingConnectionsNotUsedForPing) {
        this.poolPingConnectionsNotUsedForPing = poolPingConnectionsNotUsedForPing;
    }
}
