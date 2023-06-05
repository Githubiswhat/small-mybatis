package org.example.mybatis.mapping;

import org.example.mybatis.reflection.MetaObject;
import org.example.mybatis.session.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoundSql {

    private String sql;

    private List<ParameterMapping> parameterMappings;

    private Object parameterObject;

    private Map<String, Object> additionalParameters;

    private MetaObject metaParameters;

    public BoundSql(Configuration configuration, String sql, List<ParameterMapping> parameterMappings, Object parameterObject) {
        this.sql = sql;
        this.parameterMappings = parameterMappings;
        this.parameterObject = parameterObject;
        this.additionalParameters = new HashMap<>();
        this.metaParameters = configuration.newMetaObject(additionalParameters);
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<ParameterMapping> getParameterMappings() {
        return parameterMappings;
    }

    public void setParameterMappings(List<ParameterMapping> parameterMappings) {
        this.parameterMappings = parameterMappings;
    }

    public Object getParameterObject() {
        return parameterObject;
    }

    public void setParameterObject(Object parameterObject) {
        this.parameterObject = parameterObject;
    }

    public Map<String, Object> getAdditionalParameters() {
        return additionalParameters;
    }

    public void setAdditionalParameters(Map<String, Object> additionalParameters) {
        this.additionalParameters = additionalParameters;
    }

    public MetaObject getMetaParameters() {
        return metaParameters;
    }

    public void setMetaParameters(MetaObject metaParameters) {
        this.metaParameters = metaParameters;
    }

}
