package org.example.mybatis.executor;

import org.example.mybatis.mapping.BoundSql;
import org.example.mybatis.mapping.MappedStatement;
import org.example.mybatis.transaction.Transaction;
import org.testng.collections.Maps;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface Executor {

    <E> List<E> query(Object parameter, BoundSql boundSql, MappedStatement mappedStatement);

    void close(boolean forceRollback);

    void commit(boolean required) throws SQLException;

    void rollback(boolean required) throws SQLException;

    Transaction getTransaction() throws SQLException;

}
