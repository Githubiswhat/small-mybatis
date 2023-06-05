package org.example.mybatis.executor.statement;

import org.example.mybatis.executor.Executor;
import org.example.mybatis.mapping.BoundSql;
import org.example.mybatis.mapping.MapperStatement;
import org.example.mybatis.session.Configuration;
import org.example.mybatis.session.ResultHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class PrepareStatementHandler extends BaseStatementHandler{


    public PrepareStatementHandler(BoundSql boundSql, MapperStatement mapperStatement, Object parameterObject, Executor executor, ResultHandler resultHandler) {
        super(boundSql, mapperStatement, parameterObject, executor, resultHandler);
    }

    @Override
    protected Statement instantiateStatement(Connection connection) throws SQLException {
        String sql = boundSql.getSql();
        return connection.prepareStatement(sql);
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
        return resultSetHandler.<E>handlerResultSets(preparedStatement);
    }
}
