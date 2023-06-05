package org.example.mybatis.transaction;

import java.sql.Connection;
import java.sql.SQLException;

public interface Transaction {

    void close() throws SQLException;

    void commit() throws SQLException;

    void rollback() throws SQLException;

    Connection getConnection() throws SQLException;

}
