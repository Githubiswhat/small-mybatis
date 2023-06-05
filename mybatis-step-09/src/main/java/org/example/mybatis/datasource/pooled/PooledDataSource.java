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

    private org.slf4j.Logger logger = LoggerFactory.getLogger(PooledDataSource.class);

    private final  PooledState state = new PooledState(this);

    private final UnPooledDataSource dataSource;

    protected int poolMaximumActiveConnection = 10;

    protected int poolMaximumIdleConnection = 5;

    protected int poolMaximumCheckoutTime = 20000;

    protected int poolTimeToWait = 2000;

    protected String poolPingQuery = "NO PING QUERY SET";

    protected boolean poolPingEnabled = false;

    protected int poolPingConnectionsNotUsedFor = 0;

    private int expectedConnectionTypeCode;

    public PooledDataSource() {
        this.dataSource = new UnPooledDataSource();
    }

    public void pushConnection(PooledConnection connection) throws SQLException {
        synchronized (state){
            state.activeConnections.remove(connection);
            if (connection.isValid()){
                if (state.getIdleConnectionCount() < poolMaximumIdleConnection && connection.getConnectionTypeCode() == expectedConnectionTypeCode){
                    state.accumulatedCheckoutTime += connection.getCheckoutTime();
                    if (!connection.getRealConnection().getAutoCommit()){
                        connection.getRealConnection().rollback();
                    }
                    PooledConnection newConnection = new PooledConnection(connection.getRealConnection(), this);
                    state.idleConnections.add(newConnection);
                    newConnection.setCreateTimeStamp(connection.getCreateTimeStamp());
                    newConnection.setLastUsedTimeStamp(connection.getLastUsedTimeStamp());
                    newConnection.invalid();
                    logger.info("Return connection " + newConnection.getRealHashCode() + " to pool ");

                    state.notifyAll();
                }else{
                    state.accumulatedCheckoutTime += connection.getCheckoutTime();
                    if (!connection.getRealConnection().getAutoCommit()){
                        connection.getRealConnection().rollback();
                    }

                    connection.getRealConnection().close();
                    logger.info("Close connection " + connection.getRealHashCode() + ".");
                    connection.invalid();
                }
            }else {
                logger.info("A bad connection '" + connection.getRealConnection().hashCode() + "' attempt to return to the pool, discard the connection");
                state.badConnectionCount++;
            }
        }
    }
    public PooledConnection popConnection(String username, String password) throws SQLException{
        boolean countedWait = false;
        long t = System.currentTimeMillis();
        PooledConnection conn = null;
        int localBadConnectionCount = 0;
        while (conn == null){
            synchronized (state){
                if (! state.idleConnections.isEmpty()){
                    conn = state.idleConnections.remove(0);
                    logger.info("Checkout a connection " + conn.getRealHashCode() + "from pool");
                }else {
                    if (state.getActiveConnectionCount() < poolMaximumActiveConnection){
                        conn = new PooledConnection(dataSource.getConnection(), this);
                        logger.info("create a connection " + conn.getRealHashCode() + ".");
                    }else {
                        PooledConnection oldestConnection = state.activeConnections.get(0);
                        long longestCheckoutTime = oldestConnection.getCheckoutTime();
                        if (longestCheckoutTime > poolMaximumCheckoutTime){
                            state.claimedOverdueConnectionCount++;
                            state.accumulatedCheckoutTimeOfOverdueConnections += longestCheckoutTime;
                            state.accumulatedCheckoutTime += longestCheckoutTime;
                            state.activeConnections.remove(oldestConnection);

                            if (!oldestConnection.getRealConnection().getAutoCommit()){
                                oldestConnection.getRealConnection().rollback();
                            }

                            conn = new PooledConnection(oldestConnection.getRealConnection(), this);
                            oldestConnection.invalid();
                            logger.info("Claimed a overdue connection " + conn.getRealHashCode() + ".");
                        } else{
                            try {
                                if (!countedWait){
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
                            conn.getRealConnection().getAutoCommit();
                        }

                        conn.setConnectionTypeCode(assembleConnectionTypeCode(dataSource.getUrl(), username, password));
                        conn.setCheckoutTimeStamp(System.currentTimeMillis());
                        conn.setLastUsedTimeStamp(System.currentTimeMillis());
                        state.activeConnections.add(conn);
                        state.requestCount++;
                        state.accumulatedRequestTime += t - System.currentTimeMillis();
                    }else {
                        logger.info("Get a bad connection " + conn.getRealHashCode() +" from the pool, try to get another connection");

                        state.badConnectionCount++;
                        localBadConnectionCount++;
                        conn = null;
                        if (localBadConnectionCount > (poolMaximumIdleConnection + 3)){
                            logger.info("can not get good connection from the pool");
                            throw new SQLException("can not get good connection from the pool");
                        }
                    }
                }
            }
        }

        if (conn == null){
            logger.info("Unknown error from server. The pool return a null connection");
            throw new SQLException("Unknown error from server. The pool return a null connection");
        }

        return conn;
    }


    public void forceCloseAll() {
        expectedConnectionTypeCode = assembleConnectionTypeCode(dataSource.getUrl(), dataSource.getUsername(), dataSource.getPassword());

        for (int i = state.getIdleConnectionCount(); i > 0; i++) {
            try {
                PooledConnection conn = state.idleConnections.remove(i - 1);
                conn.invalid();

                Connection realConnection = conn.getRealConnection();
                if (realConnection.getAutoCommit()){
                    realConnection.rollback();
                }
                realConnection.close();
            } catch (SQLException e) {

            }
        }


        for (int i = state.getActiveConnectionCount(); i > 0; i++) {
            try {
                PooledConnection conn = state.activeConnections.remove(i - 1);
                conn.invalid();

                Connection realConnection = conn.getRealConnection();
                if (realConnection.getAutoCommit()){
                    realConnection.rollback();
                }
                realConnection.close();
            } catch (SQLException e) {

            }
        }

        logger.info("PooledDataSource forcefully closed / removed all connection");
    }

    public boolean pingConnection(PooledConnection connection){
        boolean result = true;

        try {
            result = !connection.getRealConnection().isClosed();
        } catch (SQLException e) {
            logger.info("Connection " + connection.getRealHashCode() + " is bad: " + e.getMessage());
            result = false;
        }

        if (result){
            if (poolPingEnabled){
                if (poolPingConnectionsNotUsedFor >= 0 && connection.getTimeElapsedSinceLastUse() > poolPingConnectionsNotUsedFor){
                    try {
                        logger.info("Testing connection " + connection.getRealHashCode() + " ...");
                        Connection realConnection = connection.getRealConnection();
                        Statement statement = realConnection.createStatement();
                        ResultSet resultSet = statement.executeQuery(poolPingQuery);
                        resultSet.close();
                        if (!realConnection.getAutoCommit()){
                            realConnection.rollback();
                        }
                        result = true;
                        logger.info("Connection " + connection.getRealHashCode() + " is Good");
                    } catch (SQLException e) {
                        logger.info("Execute of ping query " + poolPingQuery + " failed. Cause: " + e.getMessage());
                        try {
                            connection.getRealConnection().close();
                        } catch (SQLException ex) {

                        }
                        result = false;
                        logger.info("Connection " + connection.getRealHashCode() + " is Bad");
                    }
                }
            }
        }
        return result;
    }

    public static Connection unwrapConnections(Connection connection) {
        if (Proxy.isProxyClass(connection.getClass())){
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(connection);
            if (invocationHandler instanceof PooledConnection){
                return ((PooledConnection) invocationHandler).getRealConnection();
            }
        }
        return connection;
    }

    @Override
    protected void finalize() throws Throwable {
        forceCloseAll();
        super.finalize();
    }

    private int assembleConnectionTypeCode(String url, String username, String password) {
        return (url + username + password).hashCode();
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
        throw new RuntimeException(getClass().getName() + " is not a wrapper");
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
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

    public UnPooledDataSource getDataSource() {
        return dataSource;
    }

    public int getPoolMaximumActiveConnection() {
        return poolMaximumActiveConnection;
    }

    public void setPoolMaximumActiveConnection(int poolMaximumActiveConnection) {
        this.poolMaximumActiveConnection = poolMaximumActiveConnection;
    }

    public int getPoolMaximumIdleConnection() {
        return poolMaximumIdleConnection;
    }

    public void setPoolMaximumIdleConnection(int poolMaximumIdleConnection) {
        this.poolMaximumIdleConnection = poolMaximumIdleConnection;
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

    public boolean isPoolPingEnabled() {
        return poolPingEnabled;
    }

    public void setPoolPingEnabled(boolean poolPingEnabled) {
        this.poolPingEnabled = poolPingEnabled;
    }

    public int getPoolPingConnectionsNotUsedFor() {
        return poolPingConnectionsNotUsedFor;
    }

    public void setPoolPingConnectionsNotUsedFor(int poolPingConnectionsNotUsedFor) {
        this.poolPingConnectionsNotUsedFor = poolPingConnectionsNotUsedFor;
    }

    public int getExpectedConnectionTypeCode() {
        return expectedConnectionTypeCode;
    }

    public void setExpectedConnectionTypeCode(int expectedConnectionTypeCode) {
        this.expectedConnectionTypeCode = expectedConnectionTypeCode;
    }

}
