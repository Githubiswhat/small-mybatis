package org.example.mybatis.executor;

import org.example.mybatis.mapping.BoundSql;
import org.example.mybatis.mapping.MapperStatement;
import org.example.mybatis.session.Configuration;
import org.example.mybatis.session.ResultHandler;
import org.example.mybatis.transaction.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public abstract class BaseExecutor implements Executor{

    private org.slf4j.Logger logger = LoggerFactory.getLogger(BaseExecutor.class);

    private Configuration configuration;

    private Executor executor;

    protected Transaction transaction;

    private boolean closed;

    public BaseExecutor(Configuration configuration, Transaction transaction) {
        this.configuration = configuration;
        this.transaction = transaction;
        this.executor = this;
    }

    @Override
    public <E> List<E> query(MapperStatement mapperStatement, ResultHandler resultHandler, BoundSql boundSql, Object parameter) {
        if (closed){
            throw new RuntimeException("Executor is closed");
        }
        return doQuery(mapperStatement, resultHandler, boundSql, parameter);
    }

    protected abstract <E> List<E> doQuery(MapperStatement mapperStatement, ResultHandler resultHandler, BoundSql boundSql, Object parameter);

    @Override
    public void close(boolean forceRollback) {
        try {
            try {
                rollback(forceRollback);
            }finally {
                transaction.close();
            }
        } catch (SQLException e) {
            logger.warn("Unexpected exception on closing transaction. Cause: " + e);
        }finally {
            transaction = null;
            closed = true;
        }
    }

    @Override
    public void commit(boolean required) throws SQLException {
        if (closed){
            throw new SQLException("cannot commit. The transaction had closed");
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
    public Transaction getTransaction() {
        if (closed){
            throw new RuntimeException("Executor is closed");
        }
        return transaction;
    }
}
