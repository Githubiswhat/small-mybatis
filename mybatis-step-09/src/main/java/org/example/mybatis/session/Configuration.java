package org.example.mybatis.session;

import cn.hutool.core.util.ClassUtil;
import org.example.mybatis.binding.MapperProxyFactory;
import org.example.mybatis.binding.MapperRegistry;
import org.example.mybatis.datasource.druid.DruidDataSourceFactory;
import org.example.mybatis.datasource.pooled.PooledDataSourceFactory;
import org.example.mybatis.datasource.unpooled.UnPooledDataSourceFactory;
import org.example.mybatis.executor.Executor;
import org.example.mybatis.executor.SimpleExecutor;
import org.example.mybatis.executor.resuleset.DefaultResultSetHandler;
import org.example.mybatis.executor.resuleset.ResultSetHandler;
import org.example.mybatis.executor.statement.PrepareStatementHandler;
import org.example.mybatis.executor.statement.StatementHandler;
import org.example.mybatis.mapping.BoundSql;
import org.example.mybatis.mapping.Environment;
import org.example.mybatis.mapping.MappedStatement;
import org.example.mybatis.reflection.MetaObject;
import org.example.mybatis.scripting.LanguageDriverRegistry;
import org.example.mybatis.scripting.xmltags.XMLLanguageDriver;
import org.example.mybatis.transaction.Transaction;
import org.example.mybatis.transaction.jdbc.JdbcTransactionFactory;
import org.example.mybatis.type.TypeAliasRegistry;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Configuration {

    private Environment environment;

    private MapperRegistry registry = new MapperRegistry(this);

    private Map<String, MappedStatement> mappedStatements = new HashMap<>();

    private TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();

    protected final Set<String> loadedResources = new HashSet<>();

    protected final LanguageDriverRegistry languageRegistry = new LanguageDriverRegistry();

    private String databaseId;

    public Configuration() {
        typeAliasRegistry.registerAlias("JDBC", JdbcTransactionFactory.class);

        typeAliasRegistry.registerAlias("DRUID", DruidDataSourceFactory.class);
        typeAliasRegistry.registerAlias("UNPOOLED", UnPooledDataSourceFactory.class);
        typeAliasRegistry.registerAlias("POOLED", PooledDataSourceFactory.class);

        languageRegistry.setDefaultDriverClass(XMLLanguageDriver.class);

    }

    public void addMappers(String packageName){
        registry.addMappers(packageName);
    }

    public void addMapper(Class<?> type){
        registry.addMapper(type);
    }

    public boolean hasMapper(Class<?> type) {
        return registry.hasMapper(type);
    }

    public <T> T getMapper(Class<T> type, SqlSession session) {
        return registry.getMapper(type, session);
    }

    public MappedStatement getMappedStatement(String id){
        return mappedStatements.get(id);
    }

    public void addMappedStatement(MappedStatement mappedStatement){
        mappedStatements.put(mappedStatement.getId(), mappedStatement);
    }

    public boolean isResourceLoaded(String resource) {
        return loadedResources.contains(resource);
    }

    public void addLoadedResource(String resource) {
        loadedResources.add(resource);
    }






    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public MapperRegistry getRegistry() {
        return registry;
    }

    public void setRegistry(MapperRegistry registry) {
        this.registry = registry;
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

    public Executor newExecutor(Configuration configuration, Transaction transaction) {
        return new SimpleExecutor(configuration, transaction);
    }

    public StatementHandler newStatementHandler(Configuration configuration, BoundSql boundSql, Object parameterObject, MappedStatement mappedStatement) {
        return new PrepareStatementHandler(configuration, boundSql, parameterObject, mappedStatement);
    }

    public ResultSetHandler newResultSetHandler(BoundSql boundSql, MappedStatement mappedStatement) {
        return new DefaultResultSetHandler(boundSql, mappedStatement);
    }

    public Set<String> getLoadedResources() {
        return loadedResources;
    }

    public LanguageDriverRegistry getLanguageRegistry() {
        return languageRegistry;
    }

    public MetaObject newMetaObject(Object parameterObject) {
        return null;
    }

    public String getDatabaseId() {
        return databaseId;
    }
}
