package org.example.mybatis.transaction.jdbc;

import org.example.mybatis.session.TransactionIsolateLever;
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
    public Transaction newTransaction(DataSource dataSource, TransactionIsolateLever transactionIsolateLever, boolean autoCommit) {
        return new JdbcTransaction(dataSource, autoCommit, transactionIsolateLever);
    }
}
