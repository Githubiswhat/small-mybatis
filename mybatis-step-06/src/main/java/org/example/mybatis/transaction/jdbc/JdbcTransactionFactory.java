package org.example.mybatis.transaction.jdbc;

import org.example.mybatis.session.TransactionIsolateLevel;
import org.example.mybatis.transaction.Transaction;
import org.example.mybatis.transaction.TransactionFactory;

import javax.sql.DataSource;
import java.sql.Connection;

public class JdbcTransactionFactory implements TransactionFactory {
    @Override
    public Transaction newTransaction(Connection connection) {
        return new JdbcTransaction(connection);
    }

    @Override
    public Transaction newTransaction(DataSource dataSource, TransactionIsolateLevel level, boolean autoCommit) {
        return new JdbcTransaction(level, autoCommit, dataSource);
    }

}
