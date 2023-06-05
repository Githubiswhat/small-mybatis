package org.example.mybatis.transaction.jdbc;

import org.example.mybatis.session.TransactionIsolationLevel;
import org.example.mybatis.transaction.Transaction;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class JdbcTransaction implements Transaction {

    private Connection connection;

    private DataSource dataSource;

    private boolean autoCommit;

    private TransactionIsolationLevel transactionIsolateLevel;

    public JdbcTransaction(Connection connection) {
        this.connection = connection;
    }

    public JdbcTransaction(DataSource dataSource, boolean autoCommit, TransactionIsolationLevel transactionIsolateLevel) {
        this.dataSource = dataSource;
        this.autoCommit = autoCommit;
        this.transactionIsolateLevel = transactionIsolateLevel;
    }

    @Override
    public void close() throws SQLException {
        if (connection != null){
            connection.close();
        }
    }

    @Override
    public void commit() throws SQLException {
        if (connection != null && !connection.getAutoCommit()){
            connection.commit();
        }
    }

    @Override
    public void rollback() throws SQLException {
        if (connection != null && !connection.getAutoCommit()){
            connection.rollback();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (connection == null){
            connection = dataSource.getConnection();
            connection.setTransactionIsolation(transactionIsolateLevel.getLevel());
            connection.setAutoCommit(autoCommit);
        }
        return connection;
    }
}
