package org.example.mybatis.session;

import org.example.mybatis.Mapping.Environment;
import org.example.mybatis.Mapping.MappedStatement;
import org.example.mybatis.binding.MapperRegistry;
import org.example.mybatis.datasource.druid.DruidDataSourceFactory;
import org.example.mybatis.transaction.jdbc.JdbcTransactionFactory;
import org.example.mybatis.type.TypeAliasRegistry;

import java.util.HashMap;
import java.util.Map;

public class Configuration {

    protected Environment environment;

    private TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();

    private MapperRegistry mapperRegistry = new MapperRegistry(this);

    private Map<String, MappedStatement> mappedStatements = new HashMap<>();

    public Configuration() {
        typeAliasRegistry.registerAlias("JDBC", JdbcTransactionFactory.class);
        typeAliasRegistry.registerAlias("DRUID", DruidDataSourceFactory.class);
    }

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

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public TypeAliasRegistry getTypeAliasRegistry() {
        return typeAliasRegistry;
    }

    public void setTypeAliasRegistry(TypeAliasRegistry typeAliasRegistry) {
        this.typeAliasRegistry = typeAliasRegistry;
    }
}
