package org.example.mybatis.executor.resultSet;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public interface ResultSetHandler {

    <E> List<E> handlerResultSet(Statement statement) throws SQLException;

}
