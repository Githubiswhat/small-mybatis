package org.example.mybatis.Mapping;

import java.util.Map;

public class BoundSql {

    private String sql;

    private String resultType;

    private String parameterType;

    private Map<Integer, String> parameterMappings;

    public BoundSql(String sql, String resultType, String parameterType, Map<Integer, String> parameterMappings) {
        this.sql = sql;
        this.resultType = resultType;
        this.parameterType = parameterType;
        this.parameterMappings = parameterMappings;
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

    public Map<Integer, String> getParameterMappings() {
        return parameterMappings;
    }

    public void setParameterMappings(Map<Integer, String> parameterMappings) {
        this.parameterMappings = parameterMappings;
    }
}
