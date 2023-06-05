package org.example.mybatis.builder;

import org.example.mybatis.mapping.BoundSql;
import org.example.mybatis.mapping.ParameterMapping;
import org.example.mybatis.mapping.SqlSource;
import org.example.mybatis.session.Configuration;

import java.util.List;

public class StaticSqlSource implements SqlSource {

    private String sql;

    private List<ParameterMapping> parameterMappings;

    private Configuration configuration;

    public StaticSqlSource(String sql, Configuration configuration) {
        this.sql = sql;
        this.configuration = configuration;
    }

    public StaticSqlSource(String sql, List<ParameterMapping> parameterMappings, Configuration configuration) {
        this.sql = sql;
        this.parameterMappings = parameterMappings;
        this.configuration = configuration;
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        return new BoundSql(sql, parameterMappings, parameterObject, configuration);
    }
}
