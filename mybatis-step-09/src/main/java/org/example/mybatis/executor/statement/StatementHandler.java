package org.example.mybatis.executor.statement;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public interface StatementHandler {

    Statement prepare(Connection connection);

    void parameterize(Statement statement) throws SQLException;

    <E>List<E> query(Statement statement) throws SQLException;

}
