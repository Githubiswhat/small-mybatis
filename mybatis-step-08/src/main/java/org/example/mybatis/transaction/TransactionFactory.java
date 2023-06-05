package org.example.mybatis.transaction;

import org.example.mybatis.session.TransactionIsolationLevel;

import javax.sql.DataSource;
import java.sql.Connection;

public interface TransactionFactory {

    Transaction newTransactionInfo(Connection connection);

    Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel transactionIsolateLevel, boolean autoCommit);

}
