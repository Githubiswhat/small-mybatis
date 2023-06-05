package org.example.mybatis.session;

import org.example.mybatis.binding.MapperRegistry;
import org.example.mybatis.datasource.druid.DruidDataSourceFactory;
import org.example.mybatis.datasource.pooled.PooledSataSourceFactory;
import org.example.mybatis.datasource.unpooled.UnpooledDataSourceFactory;
import org.example.mybatis.mapping.Environment;
import org.example.mybatis.mapping.MappedStatement;
import org.example.mybatis.transaction.jdbc.JdbcTransactionFactory;
import org.example.mybatis.type.TypeAliasRegistry;

import java.util.HashMap;
import java.util.Map;

public class Configuration {

    private Environment environment;

    private MapperRegistry mapperRegistry = new MapperRegistry(this);

    private Map<String, MappedStatement> mappedStatements = new HashMap<>();

    private TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();

    public Configuration() {
        typeAliasRegistry.registerAlias("JDBC", JdbcTransactionFactory.class);
        typeAliasRegistry.registerAlias("DRUID", DruidDataSourceFactory.class);
        typeAliasRegistry.registerAlias("POOLED", PooledSataSourceFactory.class);
        typeAliasRegistry.registerAlias("UNPOOLED", UnpooledDataSourceFactory.class);
    }

    public void addMappedStatement(MappedStatement mappedStatement){
        mappedStatements.put(mappedStatement.getId(), mappedStatement);
    }

    public MappedStatement getMappedStatement(String id){
        return mappedStatements.get(id);
    }

    public void addMappers(String packageName){
        mapperRegistry.addMappers(packageName);
    }

    public void addMapper(Class<?> clazz){
        mapperRegistry.addMapper(clazz);
    }

    private boolean hasMapper(Class<?> clazz) {
        return mapperRegistry.hasMapper(clazz);
    }

    public <T> T getMapper(Class<T> clazz, SqlSession sqlSession){
        return mapperRegistry.getMapper(clazz, sqlSession);
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public MapperRegistry getMapperRegistry() {
        return mapperRegistry;
    }

    public void setMapperRegistry(MapperRegistry mapperRegistry) {
        this.mapperRegistry = mapperRegistry;
    }

    public Map<String, MappedStatement> getMappedStatements() {
        return mappedStatements;
    }

    public void setMappedStatements(Map<String, MappedStatement> mappedStatements) {
        this.mappedStatements = mappedStatements;
    }

    public TypeAliasRegistry getTypeAliasRegistry() {
        return typeAliasRegistry;
    }

    public void setTypeAliasRegistry(TypeAliasRegistry typeAliasRegistry) {
        this.typeAliasRegistry = typeAliasRegistry;
    }
}
