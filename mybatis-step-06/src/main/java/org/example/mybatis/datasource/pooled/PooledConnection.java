package org.example.mybatis.datasource.pooled;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

public class PooledConnection implements InvocationHandler {

    private static final String CLOSED = "close";

    private static final Class<?>[] IFACES = new Class<?>[]{Connection.class};

    private int hashCode = 0;

    private PooledDataSource dataSource;

    private Connection realConnection;

    private Connection proxyConnection;

    private long checkoutTimestamp;

    private long createdTimestamp;

    private long lastUsedTimestamp;

    private int connectionTypeCode;

    private boolean valid;

    public  PooledConnection(Connection connection, PooledDataSource dataSource){
        this.hashCode = connection.hashCode();
        this.realConnection = connection;
        this.dataSource = dataSource;
        this.createdTimestamp = System.currentTimeMillis();
        this.lastUsedTimestamp = System.currentTimeMillis();
        this.valid = true;
        this.proxyConnection = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(), IFACES, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        if (CLOSED.hashCode() == methodName.hashCode() && CLOSED.equals(methodName)){
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
        if (!valid) {
            throw new SQLException("Error accessing PooledConnection. Connection is invalid");
        }
    }

    public boolean isValid() {
        return valid && realConnection != null && dataSource.pingConnection(this);
    }

    public void invalidate(){
        valid = false;
    }

    public Connection getRealConnection() {
        return realConnection;
    }

    public Connection getProxyConnection() {
        return proxyConnection;
    }

    public int getRealHashCode() {
        return realConnection != null ? realConnection.hashCode() : 0;
    }


    public void setHashCode(int hashCode) {
        this.hashCode = hashCode;
    }

    public PooledDataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(PooledDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setRealConnection(Connection realConnection) {
        this.realConnection = realConnection;
    }

    public void setProxyConnection(Connection proxyConnection) {
        this.proxyConnection = proxyConnection;
    }

    public long getCheckoutTimestamp() {
        return checkoutTimestamp;
    }

    public void setCheckoutTimestamp(long checkoutTimestamp) {
        this.checkoutTimestamp = checkoutTimestamp;
    }

    public long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public long getLastUsedTimestamp() {
        return lastUsedTimestamp;
    }

    public void setLastUsedTimestamp(long lastUsedTimestamp) {
        this.lastUsedTimestamp = lastUsedTimestamp;
    }

    public int getConnectionTypeCode() {
        return connectionTypeCode;
    }

    public void setConnectionTypeCode(int connectionTypeCode) {
        this.connectionTypeCode = connectionTypeCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PooledConnection){
            return realConnection.hashCode() == ((PooledConnection) obj).realConnection.hashCode();
        }else if (obj instanceof Connection){
            return hashCode == obj.hashCode();
        }else {
            return false;
        }
    }

    public long getCheckoutTime() {
        return System.currentTimeMillis() - checkoutTimestamp;
    }

    public long getTimeElapsedSinceLastUse() {
        return System.currentTimeMillis() - lastUsedTimestamp;
    }



}
