package org.example.mybatis.datasource.pooled;

import org.example.mybatis.datasource.unpooled.UnpooledDataSource;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.*;
import java.util.logging.Logger;

public class PooledDataSource implements DataSource {

    private org.slf4j.Logger logger = LoggerFactory.getLogger(PooledDataSource.class);

    private final PooledState state = new PooledState(this);

    private int poolMaximumActiveConnections = 10;

    private int poolMaximumIdleConnections = 5;

    protected int poolMaximumCheckoutTime = 20000;

    private int poolTimeToWait = 2000;

    protected String poolPingQuery = "NO PING QUERY SET";

    protected int poolPingConnectionsNotUsedFor = 0;

    protected boolean poolPingEnabled = false;

    protected int expectedConnectionTypeCode;

    private final UnpooledDataSource dataSource;

    public PooledDataSource() {
        dataSource = new UnpooledDataSource();
    }


    @Override
    public Connection getConnection() throws SQLException {
        return popConnection(dataSource.getUsername(), dataSource.getPassword()).getProxyConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return popConnection(username, password).getProxyConnection();
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
        throw new SQLException(getClass().getName() + " is not a wraper");
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    public void setDriverClassName(String driver) {
        forceCloseAll();
        dataSource.setDriverClassName(driver);
    }

    public void setUrl(String url) {
        forceCloseAll();
        dataSource.setUrl(url);
    }

    public void setUsername(String username) {
        forceCloseAll();
        dataSource.setUsername(username);
    }

    public void setPassword(String password) {
        forceCloseAll();
        dataSource.setPassword(password);
    }

    public void pushConnection(PooledConnection connection) throws SQLException {
        synchronized (state){
            if (connection.isValid()){
                if (state.getIdleConnectionCount() < poolMaximumIdleConnections && connection.getConnectionTypeCode() == expectedConnectionTypeCode){
                    state.accumulatedCheckoutTime += connection.getCheckoutTime();
                    if (!connection.getRealConnection().getAutoCommit()){
                        connection.getRealConnection().rollback();
                    }
                    PooledConnection newConnection = new PooledConnection(connection.getRealConnection(), this);
                    state.idleConnections.add(newConnection);
                    newConnection.setCreateTimeStamp(connection.getCreateTimeStamp());
                    newConnection.setLastUsedTimeStamp(connection.getLastUsedTimeStamp());
                    connection.invalidate();
                    logger.info("Returned connection " + newConnection.getRealConnection() + " to pool");
                }else {
                    state.accumulatedCheckoutTime += connection.getCheckoutTime();
                    if (! connection.getRealConnection().getAutoCommit()){
                        connection.getRealConnection().rollback();
                    }
                    connection.getRealConnection().close();
                    logger.info("Closed connection " + connection.getRealHashCode() + "." );
                    connection.invalidate();
                }
            }else {
                logger.info("A bad connection (" + connection.getRealHashCode() + " ) attempted to return to the pool, discarding connection");
                state.badConnectionCount++;
            }
        }
    }


    public PooledConnection popConnection(String username, String password) throws SQLException{
        boolean countedWait = false;
        PooledConnection conn = null;
        long t = System.currentTimeMillis();
        int localBadConnectionCount = 0;

        while(conn == null){
            synchronized (state){
                if (!state.idleConnections.isEmpty()){
                    conn = state.idleConnections.remove(0);
                    logger.info("Check out connection " + conn.getRealHashCode() + " from pool.");
                } else {
                    if (state.activeConnections.size() < poolMaximumActiveConnections){
                        conn = new PooledConnection(dataSource.getConnection(), this);
                        logger.info("Create connection " + conn.getRealHashCode() + ".");
                    } else {
                        PooledConnection oldestActiveConnection = state.activeConnections.get(0);
                        long longestCheckoutTime = oldestActiveConnection.getCheckoutTime();
                        if (longestCheckoutTime > poolMaximumCheckoutTime){
                               state.claimedOverdueConnectionCount++;
                               state.accumulatedCheckoutTimeOfOverdueConnections += longestCheckoutTime;
                               state.accumulatedCheckoutTime += longestCheckoutTime;
                               state.activeConnections.remove(oldestActiveConnection);
                               if (!oldestActiveConnection.getRealConnection().getAutoCommit()){
                                   oldestActiveConnection.getRealConnection().rollback();
                               }

                               conn = new PooledConnection(oldestActiveConnection.getRealConnection(), this);
                               oldestActiveConnection.invalidate();
                               logger.info("Claimed overdue connection " + conn.getRealHashCode() + ".");
                        } else {
                            try {
                                if (! countedWait){
                                    state.hadToWaitCount++;
                                    countedWait = true;
                                }
                                logger.info("Waiting as long as " + poolTimeToWait + " milliseconds for connection");
                                long wt = System.currentTimeMillis();
                                state.wait(poolTimeToWait);
                                state.accumulatedWaitTime += System.currentTimeMillis() - wt;
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }

                if (conn != null){
                    if (conn.isValid()){
                        if (!conn.getRealConnection().getAutoCommit()){
                            conn.getRealConnection().rollback();
                        }
                        conn.setConnectionTypeCode(assembleConnectionTypeCode(dataSource.getUrl(), username, password));
                        conn.setCheckoutTimeStamp(System.currentTimeMillis());
                        conn.setLastUsedTimeStamp(System.currentTimeMillis());
                        state.activeConnections.add(conn);
                        state.requestCount++;
                        state.accumulatedRequestTime += System.currentTimeMillis() - t;
                    }else {
                        logger.info("A bad connection (" + conn.getRealHashCode() + ") was returned from the pool, getting anther connection");

                        state.badConnectionCount++;
                        localBadConnectionCount++;
                        conn = null;
                        if (localBadConnectionCount > (poolMaximumIdleConnections + 3)){
                            logger.debug("PooledDAtaSource: Could not get a good connection to the database.");
                            throw new SQLException("PooledDAtaSource: Could not get a good connection to the database.");
                        }
                    }
                }
            }
        }

        if (conn == null){
            logger.debug("PooledDataSource: Unknown severe error condition. the connection pool returned a null connection.");
            throw new SQLException("PooledDataSource: Unknown severe error condition. the connection pool returned a null connection.");
        }

        return conn;
    }

    public void forceCloseAll(){
        synchronized (state){
            expectedConnectionTypeCode = assembleConnectionTypeCode(dataSource.getUrl(), dataSource.getUsername(), dataSource.getPassword());
            for (int i = state.activeConnections.size(); i > 0; i--) {
                try{
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


            for (int i = state.idleConnections.size(); i > 0; i--) {
                try{
                    PooledConnection conn = state.idleConnections.remove(i - 1);
                    conn.invalidate();

                    Connection realConn = conn.getRealConnection();
                    if (! realConn.getAutoCommit()){
                        realConn.rollback();
                    }
                    realConn.close();
                } catch (SQLException e) {
                }
            }
            logger.info("PooledDataSource forcefully closed/removed all connections");
        }
    }


    private int assembleConnectionTypeCode(String url, String username, String password) {
        return ("" + url + username + password).hashCode();
    }

    public boolean pingConnection(PooledConnection connection) {
        boolean result = true;

        try {
            result = ! connection.getRealConnection().isClosed();
        } catch (SQLException e) {
            logger.info("Connection " + connection.getRealHashCode() + "is Bad: " + e.getMessage());
        }

        if (result){
            if (poolPingEnabled){
                if (poolPingConnectionsNotUsedFor >= 0 && connection.getTimeElapsedSinceLaseUser() > poolPingConnectionsNotUsedFor){
                    try {
                        logger.info("Testing connection " + connection.getRealHashCode()  + " ...");
                        Connection realConnection = connection.getRealConnection();
                        Statement statement = realConnection.createStatement();
                        ResultSet resultSet = statement.executeQuery(poolPingQuery);
                        resultSet.close();
                        if (! realConnection.getAutoCommit()){
                            realConnection.rollback();
                        }
                        result = true;
                        logger.info("Connection " + connection.getRealHashCode() + "is Good!");
                    } catch (SQLException e) {
                        logger.info("Execution of ping query '" + poolPingQuery + "' failed: " + e.getMessage());
                        try {
                            connection.getRealConnection().close();
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                        result = false;
                        logger.info("Connection " + connection.getRealHashCode() + " is BAD: " + e.getMessage());
                    }
                }
            }
        }
        return result;
    }


    public static Connection unWrapConnection(Connection connection){
        if (Proxy.isProxyClass(connection.getClass())){
            InvocationHandler handler = Proxy.getInvocationHandler(connection);
            if (handler instanceof PooledConnection){
                return ((PooledConnection) handler).getRealConnection();
            }
        }
        return connection;
    }

    @Override
    protected void finalize() throws Throwable {
        forceCloseAll();
        super.finalize();
    }

    public org.slf4j.Logger getLogger() {
        return logger;
    }

    public void setLogger(org.slf4j.Logger logger) {
        this.logger = logger;
    }

    public PooledState getState() {
        return state;
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

    public int getPoolMaximumCheckoutTime() {
        return poolMaximumCheckoutTime;
    }

    public void setPoolMaximumCheckoutTime(int poolMaximumCheckoutTime) {
        this.poolMaximumCheckoutTime = poolMaximumCheckoutTime;
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

    public int getPoolPingConnectionsNotUsedFor() {
        return poolPingConnectionsNotUsedFor;
    }

    public void setPoolPingConnectionsNotUsedFor(int poolPingConnectionsNotUsedFor) {
        this.poolPingConnectionsNotUsedFor = poolPingConnectionsNotUsedFor;
    }

    public boolean isPoolPingEnabled() {
        return poolPingEnabled;
    }

    public void setPoolPingEnabled(boolean poolPingEnabled) {
        this.poolPingEnabled = poolPingEnabled;
    }

    public int getExpectedConnectionTypeCode() {
        return expectedConnectionTypeCode;
    }

    public void setExpectedConnectionTypeCode(int expectedConnectionTypeCode) {
        this.expectedConnectionTypeCode = expectedConnectionTypeCode;
    }

    public UnpooledDataSource getDataSource() {
        return dataSource;
    }
}
