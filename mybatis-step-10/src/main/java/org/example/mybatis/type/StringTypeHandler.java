package org.example.mybatis.type;

import org.example.mybatis.session.Configuration;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StringTypeHandler extends BaseTypeHandler<String>{

    @Override
    protected void setNonNullParameter(PreparedStatement statement, int i, String parameter, JdbcType jdbcType) throws SQLException {
        statement.setString(i, parameter);
    }
}
