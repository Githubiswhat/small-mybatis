package org.example.mybatis.Mapping;

import org.example.mybatis.session.Configuration;
import org.example.mybatis.type.JdbcType;

public class ParameterMapping {

    private Configuration configuration;

    private String property;

    private Class<?> javaType = Object.class;

    private JdbcType jdbcType;

    public ParameterMapping() {
    }

    public static class Builder {
        private ParameterMapping parameterMapping = new ParameterMapping();

        public Builder(Configuration configuration, String property) {
            parameterMapping.configuration = configuration;
            parameterMapping.property = property;
        }

        public Builder javaType(Class<?> javaType){
            parameterMapping.javaType = javaType;
            return this;
        }

        public Builder jdbcType(JdbcType jdbcType){
            parameterMapping.jdbcType = jdbcType;
            return this;
        }

        public ParameterMapping build() {
            return parameterMapping;
        }
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public Class<?> getJavaType() {
        return javaType;
    }

    public void setJavaType(Class<?> javaType) {
        this.javaType = javaType;
    }

    public JdbcType getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(JdbcType jdbcType) {
        this.jdbcType = jdbcType;
    }
}
