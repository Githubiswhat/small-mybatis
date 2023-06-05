package org.example.mybatis.executor;

import org.example.mybatis.mapping.BoundSql;
import org.example.mybatis.mapping.MappedStatement;
import org.example.mybatis.session.Configuration;
import org.example.mybatis.session.ResultHandler;
import org.example.mybatis.transaction.Transaction;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

public abstract class BaseExecutor implements Executor {

    protected org.slf4j.Logger logger = LoggerFactory.getLogger(BaseExecutor.class);

    protected Configuration configuration;

    protected Executor wrapper;

    protected Transaction transaction;

    protected boolean closed;

    public BaseExecutor(Configuration configuration, Transaction transaction) {
        this.configuration = configuration;
        this.transaction = transaction;
        this.wrapper = this;
    }

    @Override
    public Transaction getTransaction() {
        if (closed){
            throw new RuntimeException("Executor is closed");
        }
        return this.transaction;
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
    public void commit(boolean required) throws SQLException {
        if (closed){
            throw new RuntimeException("Executor is closed");
        }
        if (required){
            transaction.commit();
        }
    }

    @Override
    public void close(boolean forceRollback) {
        try {
            try {
                rollback(forceRollback);
            } catch (SQLException e) {
                transaction.close();
            }
        } catch (SQLException e) {
            logger.warn("Unexpected exception on closing the transaction. Cause: " + e);
        }finally {
            transaction = null;
            closed = true;
        }
    }

    @Override
    public <T> List<T> query(MappedStatement mappedStatement, BoundSql boundSql, ResultHandler resultHandler, Object parameter) {
        if (closed){
            throw new RuntimeException("Executor is closed");
        }
        return doQuery(mappedStatement, boundSql, resultHandler, parameter);
    }

    protected abstract <T> List<T> doQuery(MappedStatement mappedStatement, BoundSql boundSql, ResultHandler resultSetHandler, Object parameter);


}
