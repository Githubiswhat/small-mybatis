package org.example.mybatis.type;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class DateTypeHandler extends BaseTypeHandler<Date> {

    @Override
    protected void setNonNullParameter(PreparedStatement ps, int i, Date parameter, JdbcType jdbcType) throws SQLException {
        ps.setTimestamp(i, new Timestamp(parameter.getTime()));
    }

    @Override
    protected Date getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Timestamp sqltimestamp = rs.getTimestamp(columnName);
        if (sqltimestamp != null) {
            return new Date(sqltimestamp.getTime());
        }
        return null;
    }
}
