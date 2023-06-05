package org.example.mybatis.builder;

import org.example.mybatis.mapping.ParameterMapping;
import org.example.mybatis.mapping.SqlSource;
import org.example.mybatis.parsing.GenericTokenParse;
import org.example.mybatis.parsing.TokenHandler;
import org.example.mybatis.reflection.MetaObject;
import org.example.mybatis.session.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlSourceBuilder extends BaseBuilder{

    private static final String parameterProperties = "javaType, jdbcTpe, mode, numericScale, resultMao, typeHandler, jdbcTypeName";

    public SqlSourceBuilder(Configuration configuration) {
        super(configuration);
    }

    public SqlSource parse(String originalSql, Class<?> parameterType, HashMap<String, Object> additionalParameters){
        ParameterMappingTokenHandler handler = new ParameterMappingTokenHandler(configuration, parameterType, additionalParameters);
        GenericTokenParse parser = new GenericTokenParse("#{", "}", handler);
        String sql = parser.parse(originalSql);
        return new StaticSqlSource(sql, handler.getParameterMappings(), configuration);
    }


    private static class ParameterMappingTokenHandler extends BaseBuilder implements TokenHandler {

        private List<ParameterMapping> parameterMappings = new ArrayList<>();

        private Class<?> parameterType;

        private MetaObject metaParameters;

        public ParameterMappingTokenHandler(Configuration configuration, Class<?> parameterType, Map<String, Object> additionalParameters) {
            super(configuration);
            this.parameterType = parameterType;
            this.metaParameters = configuration.newMetaObject(additionalParameters);
        }

        @Override
        public String handleToken(String content) {
            parameterMappings.add(buildParameterMapping(content));
            return "?";
        }

        private ParameterMapping buildParameterMapping(String content) {
            Map<String, String> propertiesMap = new ParameterExpression(content);
            String property = propertiesMap.get("property");
            Class<?> propertyType = parameterType;
            ParameterMapping.Builder builder = new ParameterMapping.Builder(configuration, property, propertyType);
            return builder.build();
        }

        public List<ParameterMapping> getParameterMappings() {
            return parameterMappings;
        }

        public void setParameterMappings(List<ParameterMapping> parameterMappings) {
            this.parameterMappings = parameterMappings;
        }

        public Class<?> getParameterType() {
            return parameterType;
        }

        public void setParameterType(Class<?> parameterType) {
            this.parameterType = parameterType;
        }

        public MetaObject getMetaParameters() {
            return metaParameters;
        }

        public void setMetaParameters(MetaObject metaParameters) {
            this.metaParameters = metaParameters;
        }
    }
}
