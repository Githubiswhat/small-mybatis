package org.example.mybatis.transaction.jdbc;

import org.example.mybatis.session.TransactionIsolateLevel;
import org.example.mybatis.transaction.Transaction;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;

public class JdbcTransaction implements Transaction {

    private Connection connection;

    private TransactionIsolateLevel transactionIsolateLevel = TransactionIsolateLevel.TRANSACTION_NONE;

    private boolean autoCommit;

    private DataSource dataSource;

    public JdbcTransaction(Connection connection) {
        this.connection = connection;
    }

    public JdbcTransaction(TransactionIsolateLevel transactionIsolateLevel, boolean autoCommit, DataSource dataSource) {
        this.transactionIsolateLevel = transactionIsolateLevel;
        this.autoCommit = autoCommit;
        this.dataSource = dataSource;
    }

    @Override
    public Connection getConnection() throws SQLException {
        connection = dataSource.getConnection();
        connection.setTransactionIsolation(transactionIsolateLevel.getLevel());
        connection.setAutoCommit(autoCommit);
        return connection;
    }

    @Override
    public void commit() throws SQLException {
        if (connection != null && !autoCommit){
            connection.commit();
        }
    }

    @Override
    public void rollback() throws SQLException {
        if (connection != null && !autoCommit){
            connection.rollback();
        }
    }

    @Override
    public void close() throws SQLException {
        if (connection != null && !autoCommit){
            connection.close();
        }
    }
}
