package org.example.mybatis.transaction;

import org.example.mybatis.session.TransactionIsolateLevel;

import javax.sql.DataSource;
import java.sql.Connection;

public interface TransactionFactory {

    Transaction newTransaction(Connection connection);

    Transaction newTransaction(DataSource dataSource, TransactionIsolateLevel level, boolean autoCommit);

}
