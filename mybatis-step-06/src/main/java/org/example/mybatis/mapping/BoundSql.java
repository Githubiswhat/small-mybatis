package org.example.mybatis.mapping;

import java.util.Map;

public class BoundSql {

    private String sql;

    private String resultType;

    private String parameterType;

    private Map<Integer, String> parameter;

    public BoundSql(String sql, String resultType, String parameterType, Map<Integer, String> parameter) {
        this.sql = sql;
        this.resultType = resultType;
        this.parameterType = parameterType;
        this.parameter = parameter;
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
