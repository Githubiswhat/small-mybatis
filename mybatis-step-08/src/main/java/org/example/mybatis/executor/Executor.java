package org.example.mybatis.executor;

import org.example.mybatis.mapping.BoundSql;
import org.example.mybatis.mapping.MapperStatement;
import org.example.mybatis.session.ResultHandler;
import org.example.mybatis.transaction.Transaction;

import java.sql.SQLException;
import java.util.List;

public interface Executor {

    ResultHandler NO_RESULT_HANDLER = null;

    <E> List<E> query(MapperStatement mapperStatement, ResultHandler resultHandler, BoundSql boundSql, Object parameter);

    void close(boolean forceRollback);

    void commit(boolean required) throws SQLException;

    void rollback(boolean required) throws SQLException;

    Transaction getTransaction();

}
