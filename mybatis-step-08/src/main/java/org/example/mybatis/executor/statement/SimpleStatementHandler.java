package org.example.mybatis.executor.statement;

import org.example.mybatis.executor.Executor;
import org.example.mybatis.mapping.BoundSql;
import org.example.mybatis.mapping.MapperStatement;
import org.example.mybatis.session.Configuration;
import org.example.mybatis.session.ResultHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SimpleStatementHandler extends BaseStatementHandler{


    public SimpleStatementHandler(BoundSql boundSql, MapperStatement mapperStatement, Object parameterObject, Executor executor, ResultHandler resultHandler) {
        super(boundSql, mapperStatement, parameterObject, executor, resultHandler);
    }

    @Override
    protected Statement instantiateStatement(Connection connection) throws SQLException {
        return connection.createStatement();
    }


    @Override
    public void parameterize(Statement statement) throws SQLException {

    }

    @Override
    public <E> List<E> query(Statement statement, ResultHandler resultHandler) throws SQLException {
        String sql = boundSql.getSql();
        statement.execute(sql);
        return resultSetHandler.handlerResultSets(statement);
    }
}
