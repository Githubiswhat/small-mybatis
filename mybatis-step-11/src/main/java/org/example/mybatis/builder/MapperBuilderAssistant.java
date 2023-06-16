package org.example.mybatis.builder;

import cn.hutool.core.img.ScaleType;
import org.example.mybatis.mapping.MappedStatement;
import org.example.mybatis.mapping.ResultMap;
import org.example.mybatis.mapping.SqlCommandType;
import org.example.mybatis.mapping.SqlSource;
import org.example.mybatis.scripting.LanguageDriver;
import org.example.mybatis.session.Configuration;

import java.util.ArrayList;
import java.util.List;

public class MapperBuilderAssistant extends BaseBuilder{

    private String currentNamespace;

    private String resource;

    public MapperBuilderAssistant(Configuration configuration, String resource) {
        super(configuration);
        this.resource = resource;
    }

    public String getCurrentNamespace() {
        return currentNamespace;
    }

    public void setCurrentNamespace(String currentNamespace) {
        this.currentNamespace = currentNamespace;
    }

    public String applyCurrentNamespace(String base, boolean isReference){
        if (base == null){
            return null;
        }
        if (isReference){
            if (base.contains(".")) return base;
        }
        return currentNamespace + "." + base;
    }

    public MappedStatement addMappedStatement(
            String id,
            SqlSource sqlSource,
            SqlCommandType sqlCommandType,
            Class<?> parameterType,
            String resultMap,
            Class<?> resultType,
            LanguageDriver lang
    ){
        id = applyCurrentNamespace(id, false);
        MappedStatement.Builder statementBuilder = new MappedStatement.Builder(configuration, id, sqlCommandType, sqlSource, resultType);
        setStatementResultMap(resultMap, resultType, statementBuilder);

        MappedStatement statement = statementBuilder.build();
        configuration.addMappedStatement(statement);
        return statement;
    }

    private void setStatementResultMap(String resultMap, Class<?> resultType, MappedStatement.Builder statementBuilder) {
        resultMap = applyCurrentNamespace(resultMap, true);

        List<ResultMap> resultMaps = new ArrayList<>();
        if (resultMap != null){
            //todo
        }
        else if(resultType != null){
            ResultMap.Builder inlineResultBuilder = new ResultMap.Builder(
                    configuration,
                    statementBuilder.id() + "-Inline",
                    resultType,
                    new ArrayList<>());
            resultMaps.add(inlineResultBuilder.build());
        }
        statementBuilder.resultMaps(resultMaps);
    }


}
