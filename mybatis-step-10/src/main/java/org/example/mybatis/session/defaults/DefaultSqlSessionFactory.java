package org.example.mybatis.session.defaults;

import org.example.mybatis.executor.Executor;
import org.example.mybatis.mapping.Environment;
import org.example.mybatis.session.Configuration;
import org.example.mybatis.session.SqlSession;
import org.example.mybatis.session.SqlSessionFactory;
import org.example.mybatis.session.TransactionIsolateLevel;
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
        Transaction tx = null;
        try {
            Environment environment = configuration.getEnvironment();
            DataSource dataSource = environment.getDataSource();
            TransactionFactory transactionFactory = environment.getTransactionFactory();
            tx = transactionFactory.newTransaction(dataSource, true, TransactionIsolateLevel.TRANSACTION_READ_COMMITTED);
            Executor executor = configuration.newExecutor(tx);
            return new DefaultSqlSession(configuration, executor);
        } catch (Exception e) {
            try{
                assert tx != null;
                tx.close();
            }catch (SQLException sqlException){
            }
            throw new RuntimeException("Error opening session. Cause: " + e);
        }
    }
}
