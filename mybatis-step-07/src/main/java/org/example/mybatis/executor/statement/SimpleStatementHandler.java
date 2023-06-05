package org.example.mybatis.executor.statement;

import org.example.mybatis.executor.Executor;
import org.example.mybatis.executor.resultSet.ResultSetHandler;
import org.example.mybatis.mapping.BoundSql;
import org.example.mybatis.mapping.MappedStatement;
import org.example.mybatis.session.ResultHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SimpleStatementHandler extends BaseStatementHandler{

    public SimpleStatementHandler(MappedStatement mappedStatement, BoundSql boundSql, ResultHandler resultHandler, Executor executor, Object parameterObject) {
        super(mappedStatement, boundSql, resultHandler, executor, parameterObject);
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
        statement.execute(boundSql.getSql());
        return resultSetHandler.handlerResultSet(statement);
    }
}
