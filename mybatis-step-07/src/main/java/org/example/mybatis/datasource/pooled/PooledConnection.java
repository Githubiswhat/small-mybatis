package org.example.mybatis.datasource.pooled;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

public class PooledConnection implements InvocationHandler {

    private PooledDataSource dataSource;

    private static final String CLOSED = "close";

    private static final Class<?>[] IFACES = new Class<?>[]{Connection.class};

    private int hashCode = 0;

    private long createTimeStamp;

    private long connectionTimeStamp;

    private long lastUsedTimeStamp;

    private long checkoutTimeStamp;

    private int connectionTypeCode;

    private Connection realConnection;

    private Connection proxyConnection;

    private boolean valid;

    public PooledConnection(Connection connection, PooledDataSource dataSource) {
        this.dataSource = dataSource;
        this.realConnection = connection;
        this.hashCode = connection.hashCode();

        this.createTimeStamp = System.currentTimeMillis();
        this.lastUsedTimeStamp = System.currentTimeMillis();
        this.valid = true;
        this.proxyConnection = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(), IFACES, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        if (CLOSED.hashCode() == methodName.hashCode() && CLOSED.equals(methodName)) {
            dataSource.pushConnection(this);
            return null;
        }else {
            if (! Object.class.equals(method.getDeclaringClass())){
                checkConnection();
            }
            return method.invoke(realConnection, args);
        }
    }

    private void checkConnection() throws SQLException {
        if (!valid){
            throw new SQLException("Error accessing pooled connection.  Connection is invalid.");
        }
    }

    public void invalidate(){
        valid = false;
    }

    public PooledDataSource getDataSource() {
        return dataSource;
    }

    public int getRealHashCode() {
        return realConnection == null ? 0 : realConnection.hashCode();
    }

    public long getCreateTimeStamp() {
        return createTimeStamp;
    }

    public long getConnectionTimeStamp() {
        return connectionTimeStamp;
    }

    public long getLastUsedTimeStamp() {
        return lastUsedTimeStamp;
    }

    public long getCheckoutTimeStamp() {
        return checkoutTimeStamp;
    }

    public Connection getRealConnection() {
        return realConnection;
    }

    public Connection getProxyConnection() {
        return proxyConnection;
    }

    public void setDataSource(PooledDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public int getHashCode() {
        return hashCode;
    }

    public void setHashCode(int hashCode) {
        this.hashCode = hashCode;
    }

    public void setCreateTimeStamp(long createTimeStamp) {
        this.createTimeStamp = createTimeStamp;
    }

    public void setConnectionTimeStamp(long connectionTimeStamp) {
        this.connectionTimeStamp = connectionTimeStamp;
    }

    public void setLastUsedTimeStamp(long lastUsedTimeStamp) {
        this.lastUsedTimeStamp = lastUsedTimeStamp;
    }

    public void setCheckoutTimeStamp(long checkoutTimeStamp) {
        this.checkoutTimeStamp = checkoutTimeStamp;
    }

    public int getConnectionTypeCode() {
        return connectionTypeCode;
    }

    public void setConnectionTypeCode(int connectionTypeCode) {
        this.connectionTypeCode = connectionTypeCode;
    }

    public void setRealConnection(Connection realConnection) {
        this.realConnection = realConnection;
    }

    public void setProxyConnection(Connection proxyConnection) {
        this.proxyConnection = proxyConnection;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isValid() {
        return valid && realConnection != null && dataSource.pingConnection(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof PooledConnection){
            return realConnection.hashCode() == ((PooledConnection) o).realConnection.hashCode();
        }else if (o instanceof Connection){
            return hashCode == o.hashCode();
        }else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    public long getCheckoutTime() {
        return System.currentTimeMillis() - checkoutTimeStamp;
    }

    public long getTimeElapsedSinceLaseUser() {
        return System.currentTimeMillis() - lastUsedTimeStamp;
    }
}
