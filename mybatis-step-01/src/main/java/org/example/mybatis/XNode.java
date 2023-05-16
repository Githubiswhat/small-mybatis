package org.example.mybatis;

import java.util.Map;

public class XNode {

    private String id;

    private String nameSpace;

    private String parameterType;

    private String resultType;

    private Map<Integer, String> parameters;

    private String sql;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public Map<Integer, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<Integer, String> parameters) {
        this.parameters = parameters;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }
}
