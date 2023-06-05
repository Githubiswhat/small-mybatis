package org.example.mybatis.executor.statement;

import org.example.mybatis.mapping.BoundSql;
import org.example.mybatis.mapping.MappedStatement;
import org.example.mybatis.session.Configuration;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SimpleStatementHandler extends BaseStatementHandler{


    public SimpleStatementHandler(Configuration configuration, BoundSql boundSql, Object parameterObject, MappedStatement mappedStatement) {
        super(configuration, boundSql, parameterObject, mappedStatement);
    }

    @Override
    public void parameterize(Statement statement) throws SQLException {

    }

    @Override
    public <E> List<E> query(Statement statement) throws SQLException {
        statement.execute(boundSql.getSql());
        return resultSetHandler.handlerResultSet(statement);
    }


    @Override
    protected Statement initializeStatement(Connection connection) throws SQLException {
        return connection.createStatement();
    }
}
