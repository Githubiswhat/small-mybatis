package org.example.mybatis.executor;

import org.example.mybatis.executor.statement.StatementHandler;
import org.example.mybatis.mapping.BoundSql;
import org.example.mybatis.mapping.MappedStatement;
import org.example.mybatis.session.Configuration;
import org.example.mybatis.transaction.Transaction;
import org.testng.collections.Maps;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SimpleExecutor extends BaseExecutor{

    public SimpleExecutor(Configuration configuration, Transaction transaction) {
        super(configuration, transaction);
    }

    @Override
    protected <E> List<E> doQuery(Object parameter, BoundSql boundSql, MappedStatement mappedStatement){
        try {
            StatementHandler statementHandler = configuration.newStatementHandler(configuration, boundSql, parameter, mappedStatement);
            Connection connection = transaction.getConnection();
            Statement statement = statementHandler.prepare(connection);
            statementHandler.parameterize(statement);
            return statementHandler.query(statement);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
