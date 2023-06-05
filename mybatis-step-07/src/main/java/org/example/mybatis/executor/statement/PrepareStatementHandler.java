package org.example.mybatis.executor.statement;

import org.example.mybatis.executor.Executor;
import org.example.mybatis.mapping.BoundSql;
import org.example.mybatis.mapping.MappedStatement;
import org.example.mybatis.session.ResultHandler;

import java.sql.*;
import java.util.List;

public class PrepareStatementHandler extends BaseStatementHandler {

    public PrepareStatementHandler(MappedStatement mappedStatement, BoundSql boundSql, ResultHandler resultHandler, Executor executor, Object parameterObject) {
        super(mappedStatement, boundSql, resultHandler, executor, parameterObject);
    }

    @Override
    protected Statement instantiateStatement(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSql());
        return preparedStatement;
    }

    @Override
    public void parameterize(Statement statement) throws SQLException {
        PreparedStatement preparedStatement = (PreparedStatement) statement;
        preparedStatement.setLong(1, Long.parseLong(((Object[]) parameterObject)[0].toString()));
    }

    @Override
    public <E> List<E> query(Statement statement, ResultHandler resultHandler) throws SQLException {
        PreparedStatement preparedStatement = (PreparedStatement) statement;
        preparedStatement.execute();
        return resultSetHandler.<E>handlerResultSet(preparedStatement);
    }
}
