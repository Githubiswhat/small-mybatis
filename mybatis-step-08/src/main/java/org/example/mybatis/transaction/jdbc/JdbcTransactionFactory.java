package org.example.mybatis.transaction.jdbc;

import org.example.mybatis.session.TransactionIsolationLevel;
import org.example.mybatis.transaction.Transaction;
import org.example.mybatis.transaction.TransactionFactory;

import javax.sql.DataSource;
import java.sql.Connection;

public class JdbcTransactionFactory implements TransactionFactory {


    @Override
    public Transaction newTransactionInfo(Connection connection) {
        return new JdbcTransaction(connection);
    }

    @Override
    public Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel transactionIsolateLevel, boolean autoCommit) {
        return new JdbcTransaction(dataSource, autoCommit, transactionIsolateLevel);
    }
}
