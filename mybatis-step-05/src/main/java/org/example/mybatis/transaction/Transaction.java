package org.example.mybatis.transaction;

import org.example.mybatis.session.Configuration;

import java.sql.Connection;
import java.sql.SQLException;

public interface Transaction {

    Connection getConnection() throws SQLException;

    void commit() throws SQLException;

    void rollback() throws SQLException;

    void close() throws SQLException;

}
