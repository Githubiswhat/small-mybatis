package org.example.mybatis.executor;

import org.example.mybatis.executor.statement.StatementHandler;
import org.example.mybatis.mapping.BoundSql;
import org.example.mybatis.mapping.MappedStatement;
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
    protected <T> List<T> doQuery(MappedStatement mappedStatement, BoundSql boundSql, ResultHandler resultHandler, Object parameter) {
        try {
            Configuration configuration = mappedStatement.getConfiguration();
            StatementHandler statementHandler = configuration.newStatementHandle(this, mappedStatement, boundSql, resultHandler, parameter);
            Connection connection = transaction.getConnection();
            Statement stmt = statementHandler.prepare(connection);
            statementHandler.parameterize(stmt);
            return statementHandler.query(stmt, resultHandler);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
