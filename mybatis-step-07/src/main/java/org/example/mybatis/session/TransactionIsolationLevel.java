package org.example.mybatis.session;

import java.sql.Connection;

public enum TransactionIsolationLevel {

    TRANSACTION_NONE(Connection.TRANSACTION_NONE),

    TRANSACTION_SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE),

    TRANSACTION_READ_UNCOMMITTED(Connection.TRANSACTION_READ_UNCOMMITTED),

    TRANSACTION_READ_COMMITTED(Connection.TRANSACTION_READ_COMMITTED),

    TRANSACTION_REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ);

    private int level;

    TransactionIsolationLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
