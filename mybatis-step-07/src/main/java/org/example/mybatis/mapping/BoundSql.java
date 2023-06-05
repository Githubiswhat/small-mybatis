package org.example.mybatis.mapping;

import java.util.Map;

public class BoundSql {

    private String sql;

    private String returnType;

    private String parameterType;

    private Map<Integer, String> parameter;

    public BoundSql(String sql, String returnType, String parameterType, Map<Integer, String> parameter) {
        this.sql = sql;
        this.returnType = returnType;
        this.parameterType = parameterType;
        this.parameter = parameter;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
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
