package org.example.mybatis.executor.statement;

import org.example.mybatis.mapping.BoundSql;
import org.example.mybatis.mapping.MappedStatement;
import org.example.mybatis.session.Configuration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class PrepareStatementHandler extends BaseStatementHandler{

    public PrepareStatementHandler(Configuration configuration, BoundSql boundSql, Object parameterObject, MappedStatement mappedStatement) {
        super(configuration, boundSql, parameterObject, mappedStatement);
    }

    @Override
    protected Statement initializeStatement(Connection connection) throws SQLException {
        return connection.prepareStatement(boundSql.getSql());
    }

    @Override
    public void parameterize(Statement statement) throws SQLException {
        PreparedStatement preparedStatement = (PreparedStatement) statement;
        preparedStatement.setLong(1, Long.parseLong(((Object[]) parameterObject)[0].toString()));
    }

    @Override
    public <E> List<E> query(Statement statement) throws SQLException {
        PreparedStatement preparedStatement = (PreparedStatement) statement;
        preparedStatement.execute();
        return resultSetHandler.<E>handlerResultSet(statement);
    }

}
