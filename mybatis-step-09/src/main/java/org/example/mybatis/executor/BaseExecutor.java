package org.example.mybatis.executor;

import org.example.mybatis.mapping.BoundSql;
import org.example.mybatis.mapping.MappedStatement;
import org.example.mybatis.session.Configuration;
import org.example.mybatis.transaction.Transaction;

import java.sql.SQLException;
import java.util.List;

public abstract class BaseExecutor implements Executor{
    private boolean closed;

    protected Configuration configuration;
    protected Transaction transaction;

    protected Executor executor;

    public BaseExecutor(Configuration configuration, Transaction transaction) {
        this.configuration = configuration;
        this.transaction = transaction;
        this.executor = this;
    }

    @Override
    public <E> List<E> query( Object parameter, BoundSql boundSql, MappedStatement mappedStatement) {
        if (closed){
            throw new RuntimeException("the executor is closed");
        }
        return doQuery(parameter, boundSql, mappedStatement);
    }

    protected abstract <E> List<E> doQuery(Object parameter, BoundSql boundSql, MappedStatement mappedStatement);

    @Override
    public void close(boolean forceRollback) {
        if (!closed){
            try {
                try {
                    rollback(forceRollback);
                }finally {
                    transaction.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }finally {
                transaction = null;
                closed = true;
            }
        }
    }

    @Override
    public void commit(boolean required) throws SQLException {
        if (closed){
            throw new RuntimeException("the executor is closed");
        }
        if (required){
            transaction.commit();
        }
    }

    @Override
    public void rollback(boolean required) throws SQLException {
        if (!closed){
            if (required){
                transaction.rollback();
            }
        }
    }

    @Override
    public Transaction getTransaction() throws SQLException {
        return transaction;
    }
}
