package org.example.mybatis.mapping;

import org.example.mybatis.session.Configuration;
import org.example.mybatis.type.JdbcType;
import org.example.mybatis.type.TypeHandler;
import org.example.mybatis.type.TypeHandlerRegistry;

import java.util.ArrayList;
import java.util.List;

public class ResultMapping {

    private Configuration configuration;
    private String property;
    private String column;
    private Class<?> javaType;
    private JdbcType jdbcType;
    private TypeHandler<?> typeHandler;

    private List<ResultFlag> flags;

    ResultMapping() {
    }

    public static class Builder {
        private ResultMapping resultMapping = new ResultMapping();

        public Builder(Configuration configuration, String property, String column, Class<?> javaType){
            resultMapping.configuration = configuration;
            resultMapping.property = property;
            resultMapping.column = column;
            resultMapping.javaType = javaType;
            resultMapping.flags = new ArrayList<>();
        }

        public Builder typeHandler(TypeHandler<?> typeHandler){
            resultMapping.typeHandler = typeHandler;
            return this;
        }

        public Builder flags(List<ResultFlag> flags){
            resultMapping.flags = flags;
            return this;
        }

        public ResultMapping build() {
            resolverTypeHandler();
            return resultMapping;
        }

        private void resolverTypeHandler() {
            if (resultMapping.typeHandler == null && resultMapping.javaType != null){
                Configuration configuration = resultMapping.configuration;
                TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
                resultMapping.typeHandler =  typeHandlerRegistry.getTypeHandler(resultMapping.javaType, null);
            }
        }
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public String getProperty() {
        return property;
    }

    public String getColumn() {
        return column;
    }

    public Class<?> getJavaType() {
        return javaType;
    }

    public JdbcType getJdbcType() {
        return jdbcType;
    }

    public TypeHandler<?> getTypeHandler() {
        return typeHandler;
    }
}
