package org.example.mybatis.datasource.pooled;


import java.util.ArrayList;
import java.util.List;

public class PooledState {

    protected PooledDataSource dataSource;

    protected final List<PooledConnection> idleConnections = new ArrayList<>();

    protected final List<PooledConnection> activeConnections = new ArrayList<>();

    protected long requestCount = 0;

    protected long accumulatedRequestTime = 0;

    protected long accumulatedCheckoutTime = 0;

    protected long claimedOverdueConnectionCount = 0;

    protected long accumulatedCheckoutTimeOfOverdueConnections = 0;

    protected long accumulatedWaitTime = 0;

    protected long hadToWaitTime = 0;

    protected long badConnectCount = 0;


    public PooledState(PooledDataSource pooledDataSource) {
        this.dataSource = pooledDataSource;
    }

    public synchronized long getRequestCount() {
        return requestCount;
    }

    public synchronized long getAverageRequestTime() {
        return requestCount == 0 ? 0 : accumulatedRequestTime / requestCount;
    }

    public synchronized long getHadToWaitTime() {
        return hadToWaitTime;
    }

    public synchronized long getAverageWaitTime() {
        return hadToWaitTime == 0 ? 0 : accumulatedWaitTime / hadToWaitTime;
    }

    public synchronized long getBadConnectCount() {
        return badConnectCount;
    }

    public synchronized long getClaimedOverdueConnectionCount() {
        return claimedOverdueConnectionCount;
    }

    public synchronized long getAverageOverDueCheckoutTime(){
        return claimedOverdueConnectionCount == 0 ? 0 : accumulatedCheckoutTimeOfOverdueConnections / claimedOverdueConnectionCount;
    }

    public synchronized long getAverageCheckOutTime(){
        return requestCount ==  0 ? 0 : accumulatedCheckoutTime / requestCount;
    }

    public synchronized int getIdleConnectionCount(){
        return idleConnections.size();
    }

    public synchronized int getActiveConnectionCount(){
        return activeConnections.size();
    }
}
