package org.example.mybatis.type;

import org.example.mybatis.session.Configuration;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LongTypeHandler extends BaseTypeHandler<Long>{

    @Override
    protected void setNonNullParameter(PreparedStatement statement, int i, Long parameter, JdbcType jdbcType) throws SQLException {
        statement.setLong(i, parameter);
    }
}
