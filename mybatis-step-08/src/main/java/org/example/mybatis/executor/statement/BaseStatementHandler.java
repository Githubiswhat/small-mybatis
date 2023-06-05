package org.example.mybatis.executor.statement;

import org.example.mybatis.executor.Executor;
import org.example.mybatis.executor.resultSet.ResultSetHandler;
import org.example.mybatis.mapping.BoundSql;
import org.example.mybatis.mapping.MapperStatement;
import org.example.mybatis.session.Configuration;
import org.example.mybatis.session.ResultHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class BaseStatementHandler implements StatementHandler{

    protected Configuration configuration;

    protected BoundSql boundSql;

    protected MapperStatement mapperStatement;

    protected Object parameterObject;

    protected Executor executor;

    protected ResultSetHandler resultSetHandler;

    public BaseStatementHandler(BoundSql boundSql, MapperStatement mapperStatement, Object parameterObject, Executor executor, ResultHandler resultHandler) {
        this.configuration = mapperStatement.getConfiguration();
        this.boundSql = boundSql;
        this.mapperStatement = mapperStatement;
        this.parameterObject = parameterObject;

        this.executor = executor;
        this.resultSetHandler = configuration.newResultSetHandler(executor, mapperStatement, boundSql);
    }


    @Override
    public Statement prepare(Connection connection) throws SQLException {
        Statement statement = null;
        try {
            statement = instantiateStatement(connection);
            statement.setQueryTimeout(350);
            statement.setFetchSize(10000);
            return statement;
        } catch (Exception e) {
            throw new SQLException("Error preparing statement. Cause: " + e, e);
        }
    }

    protected abstract Statement instantiateStatement(Connection connection) throws SQLException;



}
