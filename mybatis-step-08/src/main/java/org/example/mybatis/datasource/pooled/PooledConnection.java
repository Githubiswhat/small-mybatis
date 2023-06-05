package org.example.mybatis.datasource.pooled;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

public class PooledConnection implements InvocationHandler {

    private long checkoutTimeStamp;

    private long createTimeStamp;

    private long lastUsedTimeStamp;

    private long connectionTimeStamp;

    private final static String CLOSE = "close";

    private final static Class<?>[]  IFACES = new Class[]{Connection.class};

    private Connection realConnection;

    private Connection proxyConnection;

    private int hashCode = 0;

    private PooledDataSource dataSource;

    private boolean valid;

    protected int connectionTypeCode;


    public PooledConnection(Connection connection, PooledDataSource dataSource) {
        this.realConnection = connection;
        this.dataSource = dataSource;
        this.hashCode = connection.hashCode();
        this.valid = true;

        this.createTimeStamp = System.currentTimeMillis();
        this.lastUsedTimeStamp = System.currentTimeMillis();
        this.proxyConnection = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(), IFACES, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        if (CLOSE.hashCode() == methodName.hashCode() && CLOSE.equals(methodName)){
            dataSource.pushConnection(this);
            return null;
        }else {
            if (! Object.class.equals(method.getDeclaringClass())){
                checkConnection();
            }
            return method.invoke(realConnection, args);
        }
    }

    private void checkConnection() {
        if (!valid){
            throw new RuntimeException("Error accessing the poolConnection, the connection is valid");
        }
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PooledConnection){
            return realConnection.hashCode() == ((PooledConnection) obj).realConnection.hashCode();
        }else if (obj instanceof Connection){
            return hashCode == obj.hashCode();
        }else{
            return false;
        }
    }

    public long getCheckoutTimeStamp() {
        return checkoutTimeStamp;
    }

    public void setCheckoutTimeStamp(long checkoutTimeStamp) {
        this.checkoutTimeStamp = checkoutTimeStamp;
    }

    public long getCreateTimeStamp() {
        return createTimeStamp;
    }

    public void setCreateTimeStamp(long createTimeStamp) {
        this.createTimeStamp = createTimeStamp;
    }

    public long getLastUsedTimeStamp() {
        return lastUsedTimeStamp;
    }

    public void setLastUsedTimeStamp(long lastUsedTimeStamp) {
        this.lastUsedTimeStamp = lastUsedTimeStamp;
    }

    public long getConnectionTimeStamp() {
        return connectionTimeStamp;
    }

    public void setConnectionTimeStamp(long connectionTimeStamp) {
        this.connectionTimeStamp = connectionTimeStamp;
    }

    public Connection getRealConnection() {
        return realConnection;
    }

    public void setRealConnection(Connection realConnection) {
        this.realConnection = realConnection;
    }

    public Connection getProxyConnection() {
        return proxyConnection;
    }

    public void setProxyConnection(Connection proxyConnection) {
        this.proxyConnection = proxyConnection;
    }

    public int getHashCode() {
        return hashCode;
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

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public void invalid(){
        valid = false;
    }

    public int getConnectionTypeCode() {
        return connectionTypeCode;
    }

    public void setConnectionTypeCode(int connectionTypeCode) {
        this.connectionTypeCode = connectionTypeCode;
    }

    public long getCheckoutTime() {
        return System.currentTimeMillis() - checkoutTimeStamp;
    }

    public int getRealHashCode() {
        return realConnection.hashCode();
    }

    public long getTimeElapsedSinceLastUse() {
        return System.currentTimeMillis() - lastUsedTimeStamp;
    }
}
