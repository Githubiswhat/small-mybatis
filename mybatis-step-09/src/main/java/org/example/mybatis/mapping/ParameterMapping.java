package org.example.mybatis.mapping;

import org.example.mybatis.session.Configuration;
import org.example.mybatis.type.JdbcType;

public class ParameterMapping {

    private Configuration configuration;

    private String property;

    private Class<?> javaType = Object.class;

    private JdbcType jdbcType;

    public ParameterMapping() {
    }

    public static class Builder{
        private ParameterMapping parameterMapping = new ParameterMapping();

        public Builder(Configuration configuration, String property, Class<?> javaType) {
            parameterMapping.configuration = configuration;
            parameterMapping.property = property;
            parameterMapping.javaType = javaType;
        }

        public Builder javaType(Class<?> javaType){
            parameterMapping.javaType = javaType;
            return this;
        }

        public Builder jdbcType(JdbcType jdbcType){
            parameterMapping.jdbcType = jdbcType;
            return this;
        }

        public ParameterMapping build(){
            return parameterMapping;
        }
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public String getProperty() {
        return property;
    }

    public Class<?> getJavaType() {
        return javaType;
    }

    public JdbcType getJdbcType() {
        return jdbcType;
    }
}
