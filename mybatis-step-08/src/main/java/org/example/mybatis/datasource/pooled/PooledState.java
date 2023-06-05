package org.example.mybatis.datasource.pooled;

import java.util.ArrayList;
import java.util.List;

public class PooledState {

    protected PooledDataSource dataSource;

    protected final List<PooledConnection> idleConnections = new ArrayList<PooledConnection>();

    protected final List<PooledConnection> activeConnections = new ArrayList<PooledConnection>();

    protected long requestCount = 0;

    protected long accumulatedRequestTime = 0;

    protected long accumulatedCheckoutTime = 0;

    protected long claimedOverdueConnectionCount = 0;

    protected long accumulatedCheckoutTimeOfOverdueConnections = 0;

    protected long accumulatedWaitTime = 0;

    protected long hadToWaitCount = 0;

    protected long badConnectionCount = 0;

    public PooledState(PooledDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public PooledDataSource getDataSource() {
        return dataSource;
    }

    public List<PooledConnection> getIdleConnections() {
        return idleConnections;
    }

    public List<PooledConnection> getActiveConnections() {
        return activeConnections;
    }

    public synchronized long getRequestCount() {
        return requestCount;
    }

    public synchronized long getAverageRequestTime() {
        return requestCount == 0 ? 0 : accumulatedRequestTime / requestCount;
    }

    public synchronized long getAverageWaitTime() {
        return requestCount == 0 ? 0 : accumulatedWaitTime / requestCount;
    }

    public long getAccumulatedResponseTime() {
        return accumulatedRequestTime;
    }

    public long getAccumulatedCheckoutTime() {
        return accumulatedCheckoutTime;
    }

    public long getClaimedOverdueConnectionCount() {
        return claimedOverdueConnectionCount;
    }

    public long getAccumulatedCheckoutTimeOfOverdueConnections() {
        return accumulatedCheckoutTimeOfOverdueConnections;
    }

    public long getAccumulatedWaitTime() {
        return accumulatedWaitTime;
    }

    public long getHadToWaitCount() {
        return hadToWaitCount;
    }

    public long getBadConnectionCount() {
        return badConnectionCount;
    }

    public synchronized long getAverageOverdueCheckoutTime(){
        return claimedOverdueConnectionCount == 0 ? 0 : accumulatedCheckoutTimeOfOverdueConnections / claimedOverdueConnectionCount;
    }

    public synchronized long getAverageCheckoutTime(){
        return requestCount == 0 ? 0 : accumulatedCheckoutTime / requestCount;
    }

    public synchronized int getIdleConnectionCount(){
        return idleConnections.size();
    }

    public synchronized int getActiveConnectionCount(){
        return activeConnections.size();
    }

}
