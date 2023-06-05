package org.example.mybatis.executor.statement;

import org.example.mybatis.executor.Executor;
import org.example.mybatis.executor.resultSet.ResultSetHandler;
import org.example.mybatis.mapping.BoundSql;
import org.example.mybatis.mapping.MappedStatement;
import org.example.mybatis.session.Configuration;
import org.example.mybatis.session.ResultHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class BaseStatementHandler implements StatementHandler{

    protected Configuration configuration;

    protected MappedStatement mappedStatement;

    protected BoundSql boundSql;

    protected ResultSetHandler resultSetHandler;

    protected Executor executor;

    protected Object parameterObject;

    public BaseStatementHandler(MappedStatement mappedStatement, BoundSql boundSql, ResultHandler resultHandler, Executor executor, Object parameterObject) {
        this.configuration = mappedStatement.getConfiguration();
        this.mappedStatement = mappedStatement;
        this.boundSql = boundSql;

        this.resultSetHandler = configuration.newResultSetHandler(executor, mappedStatement, boundSql);
        this.executor = executor;
        this.parameterObject = parameterObject;
    }

    @Override
    public Statement prepare(Connection connection) throws SQLException {
        Statement statement = null;
        try {
            statement = instantiateStatement(connection);
            statement.setFetchSize(10000);
            statement.setQueryTimeout(350);
            return statement;
        } catch (SQLException e) {
            throw new RuntimeException("Error preparing statement. Cause: " + e, e);
        }
    }

    protected abstract Statement instantiateStatement(Connection connection) throws SQLException;


}
