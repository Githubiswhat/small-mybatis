package org.example.mybatis.session;

import org.example.mybatis.Mapping.MappedStatement;
import org.example.mybatis.binding.MapperRegistry;

import java.util.HashMap;
import java.util.Map;

public class Configuration {

    private MapperRegistry mapperRegistry = new MapperRegistry(this);

    private Map<String, MappedStatement> mappedStatements = new HashMap<>();

    public void addMapper(Class<?> type){
        mapperRegistry.addMapper(type);
    }

    public void addMappers(String packageName){
        mapperRegistry.addMappers(packageName);
    }

    public <T> T getMapper(Class<?> type, SqlSession session){
        return mapperRegistry.getMapper(type, session);
    }

    public boolean hasMapper(Class<?> type){
        return mapperRegistry.hasMapper(type);
    }


    public MappedStatement getMappedStatement(String id){
        return mappedStatements.get(id);
    }

    public void addMappedStatement(MappedStatement mappedStatement) {
        mappedStatements.put(mappedStatement.getId(), mappedStatement);
    }
}
