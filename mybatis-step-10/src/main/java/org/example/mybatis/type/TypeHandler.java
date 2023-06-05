package org.example.mybatis.type;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface TypeHandler<T> {

    void setParameter(PreparedStatement statement, int i,  T Parameter, JdbcType jdbcType) throws SQLException;

}
