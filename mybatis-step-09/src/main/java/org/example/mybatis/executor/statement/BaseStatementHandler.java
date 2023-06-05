package org.example.mybatis.executor.statement;

import org.example.mybatis.executor.resuleset.ResultSetHandler;
import org.example.mybatis.mapping.BoundSql;
import org.example.mybatis.mapping.MappedStatement;
import org.example.mybatis.session.Configuration;
import org.testng.collections.Maps;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public abstract class BaseStatementHandler implements StatementHandler{

    protected ResultSetHandler resultSetHandler;

    protected BoundSql boundSql;

    protected Object parameterObject;

    public BaseStatementHandler(Configuration configuration, BoundSql boundSql, Object parameterObject, MappedStatement mappedStatement) {
        this.resultSetHandler = configuration.newResultSetHandler(boundSql, mappedStatement);
        this.boundSql = boundSql;
        this.parameterObject = parameterObject;
    }

    @Override
    public Statement prepare(Connection connection) {
        Statement statement = null;
        try {
            statement = initializeStatement(connection);
            statement.setFetchSize(200);
            statement.setQueryTimeout(10000);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return statement;
    }

    protected abstract Statement initializeStatement(Connection connection) throws SQLException;

}
