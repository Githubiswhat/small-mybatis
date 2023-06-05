package org.example.mybatis.executor;

import org.example.mybatis.executor.resultSet.ResultSetHandler;
import org.example.mybatis.mapping.BoundSql;
import org.example.mybatis.mapping.MappedStatement;
import org.example.mybatis.session.ResultHandler;
import org.example.mybatis.transaction.Transaction;

import java.sql.SQLException;
import java.util.List;

public interface Executor {

    ResultHandler NO_RESULT_HANDLER = null;

    Transaction getTransaction();

    void rollback(boolean required) throws SQLException;

    void commit(boolean required) throws SQLException;

    void close(boolean forceRollback);

    <T> List<T> query(MappedStatement mappedStatement, BoundSql boundSql, ResultHandler resultHandler, Object parameter);

}
