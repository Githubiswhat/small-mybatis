package org.example.mybatis.mapping;

import org.example.mybatis.session.Configuration;
import org.example.mybatis.type.JdbcType;
import org.example.mybatis.type.TypeHandler;
import org.example.mybatis.type.TypeHandlerRegistry;

public class ParameterMapping {

    private Configuration configuration;

    // property
    private String property;
    // javaType = int
    private Class<?> javaType = Object.class;
    // jdbcType=NUMERIC
    private JdbcType jdbcType;
    private TypeHandler<?> typeHandler;

    private ParameterMapping() {
    }

    public static class Builder {

        private ParameterMapping parameterMapping = new ParameterMapping();

        public Builder(Configuration configuration, String property, Class<?> javaType) {
            parameterMapping.configuration = configuration;
            parameterMapping.property = property;
            parameterMapping.javaType = javaType;
        }

        public Builder javaType(Class<?> javaType) {
            parameterMapping.javaType = javaType;
            return this;
        }

        public Builder jdbcType(JdbcType jdbcType) {
            parameterMapping.jdbcType = jdbcType;
            return this;
        }

        public ParameterMapping build() {
            if (parameterMapping.typeHandler == null && parameterMapping.javaType != null) {
                Configuration configuration = parameterMapping.configuration;
                TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
                parameterMapping.typeHandler = typeHandlerRegistry.getTypeHandler(parameterMapping.javaType, parameterMapping.jdbcType);
            }

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

    public TypeHandler<?> getTypeHandler() {
        return typeHandler;
    }

    public void setTypeHandler(TypeHandler<?> typeHandler) {
        this.typeHandler = typeHandler;
    }
}
