package org.example.mybatis.executor;

import org.example.mybatis.executor.statement.StatementHandler;
import org.example.mybatis.mapping.BoundSql;
import org.example.mybatis.mapping.MapperStatement;
import org.example.mybatis.session.Configuration;
import org.example.mybatis.session.ResultHandler;
import org.example.mybatis.transaction.Transaction;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SimpleExecutor extends BaseExecutor{


    public SimpleExecutor(Configuration configuration, Transaction transaction) {
        super(configuration, transaction);
    }

    @Override
    protected <E> List<E> doQuery(MapperStatement mapperStatement, ResultHandler resultHandler, BoundSql boundSql, Object parameter) {
        try {
            Configuration configuration = mapperStatement.getConfiguration();
            StatementHandler statementHandler = configuration.newStatementHandler(mapperStatement, boundSql, resultHandler, this, parameter);
            Connection connection = transaction.getConnection();
            Statement statement = statementHandler.prepare(connection);
            statementHandler.parameterize(statement);
            return statementHandler.query(statement, resultHandler);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
