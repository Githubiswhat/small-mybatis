package org.example.mybatis.datasource.pooled;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class PooledState {

    protected List<PooledConnection> activeConnections = new ArrayList<>();

    protected List<PooledConnection> idleConnections = new ArrayList<>();

    protected long requestCount = 0;

    protected long accumulatedRequestTime = 0;

    protected long accumulatedCheckoutTime = 0;

    protected long claimedOverdueConnectionCount = 0;

    protected long accumulatedCheckoutTimeOfOverdueConnections = 0;

    protected long hadToWaitCount = 0;

    protected long accumulatedWaitTime = 0;

    protected long badConnectionCount = 0;

    protected DataSource dataSource;

    public PooledState(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public synchronized long getRequestCount() {
        return requestCount;
    }

    public synchronized long getAverageRequestTime() {
        return requestCount == 0 ? 0 : accumulatedRequestTime / requestCount;
    }

    public synchronized long getAccumulatedWaitTime(){
        return hadToWaitCount == 0 ? 0 : accumulatedWaitTime / hadToWaitCount;
    }

    public synchronized long getClaimedOverdueConnectionCount() {
        return claimedOverdueConnectionCount;
    }

    public synchronized long getHadToWaitCount() {
        return hadToWaitCount;
    }

    public synchronized long getBadConnectionCount() {
        return badConnectionCount;
    }

    public synchronized long getAverageOverdueCheckoutTime() {
        return claimedOverdueConnectionCount == 0 ? 0 : accumulatedCheckoutTimeOfOverdueConnections / claimedOverdueConnectionCount;
    }

    public synchronized long getAccumulatedCheckoutTime(){
        return requestCount == 0 ? 0 : accumulatedCheckoutTime / requestCount;
    }

    public synchronized int getIdleConnectionCount() {
        return idleConnections.size();
    }

    public synchronized int getActiveConnectCount(){
        return activeConnections.size();
    }

}
