package org.example.mybatis.session.defaults;

import org.example.mybatis.executor.Executor;
import org.example.mybatis.mapping.Environment;
import org.example.mybatis.session.Configuration;
import org.example.mybatis.session.SqlSession;
import org.example.mybatis.session.SqlSessionFactory;
import org.example.mybatis.transaction.Transaction;
import org.example.mybatis.transaction.TransactionFactory;

import javax.sql.DataSource;
import java.sql.SQLException;

public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        Transaction transaction = null;
        try {
            Environment environment = configuration.getEnvironment();
            TransactionFactory transactionFactory = environment.getTransactionFactory();
            DataSource dataSource = environment.getDataSource();
            transaction = transactionFactory.newTransaction(dataSource.getConnection());
            Executor executor = configuration.newExecutor(configuration, transaction);
            return new DefaultSqlSession(configuration, executor);
        } catch (Exception e) {
            try {
                assert transaction != null;
                transaction.close();
            } catch (SQLException ignore) {
            }
            throw new RuntimeException("Error opening session.  Cause: " + e);
        }

    }
}
