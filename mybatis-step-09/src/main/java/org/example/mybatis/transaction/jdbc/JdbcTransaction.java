package org.example.mybatis.transaction.jdbc;

import org.example.mybatis.session.TransactionIsolateLever;
import org.example.mybatis.transaction.Transaction;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class JdbcTransaction implements Transaction {

    private Connection connection;

    private DataSource dataSource;

    private boolean autoCommit;

    private TransactionIsolateLever transactionIsolateLever;

    public JdbcTransaction(Connection connection) {
        this.connection = connection;
    }

    public JdbcTransaction(DataSource dataSource, boolean autoCommit, TransactionIsolateLever transactionIsolateLever) {
        this.dataSource = dataSource;
        this.autoCommit = autoCommit;
        this.transactionIsolateLever = transactionIsolateLever;
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (connection == null){
            connection = dataSource.getConnection();
            connection.setTransactionIsolation(transactionIsolateLever.getLevel());
            connection.setAutoCommit(autoCommit);
        }
        return connection;
    }

    @Override
    public void commit() throws SQLException {
        if (connection != null && !connection.getAutoCommit()){
            connection.commit();
        }
    }

    @Override
    public void close() throws SQLException {
        if (connection != null && !connection.getAutoCommit()){
            connection.close();
        }
    }

    @Override
    public void rollback() throws SQLException {
        if (connection != null && !connection.getAutoCommit()){
            connection.rollback();
        }
    }
}
