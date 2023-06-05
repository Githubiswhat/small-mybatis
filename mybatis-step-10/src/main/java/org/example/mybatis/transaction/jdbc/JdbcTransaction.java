package org.example.mybatis.transaction.jdbc;

import org.example.mybatis.session.TransactionIsolateLevel;
import org.example.mybatis.transaction.Transaction;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class JdbcTransaction implements Transaction {

    private Connection connection;

    private DataSource dataSource;

    private boolean autoCommit;

    private TransactionIsolateLevel transactionIsolateLevel = TransactionIsolateLevel.TRANSACTION_NONE;

    public JdbcTransaction(Connection connection) {
        this.connection = connection;
    }

    public JdbcTransaction(DataSource dataSource, boolean autoCommit, TransactionIsolateLevel transactionIsolateLevel) {
        this.dataSource = dataSource;
        this.autoCommit = autoCommit;
        this.transactionIsolateLevel = transactionIsolateLevel;
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (connection == null){
            connection = dataSource.getConnection();
            connection.setAutoCommit(autoCommit);
            connection.setTransactionIsolation(transactionIsolateLevel.getLevel());
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
    public void rollback() throws SQLException {
        if (connection != null && !connection.getAutoCommit()){
            connection.rollback();
        }

    }

    @Override
    public void close() throws SQLException {
        if (connection != null && !connection.getAutoCommit()){
            connection.close();
        }
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean isAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    public TransactionIsolateLevel getTransactionIsolateLevel() {
        return transactionIsolateLevel;
    }

    public void setTransactionIsolateLevel(TransactionIsolateLevel transactionIsolateLevel) {
        this.transactionIsolateLevel = transactionIsolateLevel;
    }
}
