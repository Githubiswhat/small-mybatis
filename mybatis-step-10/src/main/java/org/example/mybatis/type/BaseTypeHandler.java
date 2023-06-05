package org.example.mybatis.type;

import org.example.mybatis.session.Configuration;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class BaseTypeHandler<T> implements TypeHandler<T> {

    private Configuration configuration;

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void setParameter(PreparedStatement statement, int i, T Parameter, JdbcType jdbcType) throws SQLException {
        setNonNullParameter(statement, i, Parameter, jdbcType);
    }

    protected abstract void setNonNullParameter(PreparedStatement statement, int i, T parameter, JdbcType jdbcType) throws SQLException;
}
