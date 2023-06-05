package org.example.mybatis.executor.statement;

import org.example.mybatis.session.ResultHandler;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface StatementHandler {

    Statement prepare(Connection connection) throws SQLException;

    void parameterize(Statement statement) throws SQLException;

    <E>List<E> query(Statement statement, ResultHandler resultHandler) throws SQLException;

}
