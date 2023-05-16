package org.example.mybatis.mapping;

import org.example.mybatis.session.Configuration;

import java.util.Map;

public class MappedStatement {

    private Configuration configuration;

    private SqlCommandType sqlCommandType;

    private String id;

    private String sql;

    private String resultType;

    private String parameterType;

    private Map<Integer, String> parameter;

    public static class Builder {

        private MappedStatement mappedStatement = new MappedStatement();

        public Builder(Configuration configuration, SqlCommandType sqlCommandType, String id, String sql, String resultType, String parameterType, Map<Integer, String> parameter) {
            mappedStatement.configuration = configuration;
            mappedStatement.sqlCommandType = sqlCommandType;
            mappedStatement.id = id;
            mappedStatement.sql = sql;
            mappedStatement.resultType = resultType;
            mappedStatement.parameterType = parameterType;
            mappedStatement.parameter = parameter;
        }

        public MappedStatement build(){
            assert mappedStatement.configuration != null;
            assert mappedStatement.id != null;
            return mappedStatement;
        }

    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public SqlCommandType getSqlCommandType() {
        return sqlCommandType;
    }

    public void setSqlCommandType(SqlCommandType sqlCommandType) {
        this.sqlCommandType = sqlCommandType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    public Map<Integer, String> getParameter() {
        return parameter;
    }

    public void setParameter(Map<Integer, String> parameter) {
        this.parameter = parameter;
    }
}
