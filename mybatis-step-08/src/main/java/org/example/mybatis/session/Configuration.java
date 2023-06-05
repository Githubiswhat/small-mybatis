package org.example.mybatis.session;

import cn.hutool.core.util.ClassUtil;
import org.example.mybatis.binding.MapperProxyFactory;
import org.example.mybatis.binding.MapperRegistry;
import org.example.mybatis.datasource.pooled.PooledDataSourceFactory;
import org.example.mybatis.datasource.unpooled.UnPooledDataSourceFactory;
import org.example.mybatis.executor.Executor;
import org.example.mybatis.executor.SimpleExecutor;
import org.example.mybatis.executor.resultSet.DefaultResultSetHandler;
import org.example.mybatis.executor.resultSet.ResultSetHandler;
import org.example.mybatis.executor.statement.PrepareStatementHandler;
import org.example.mybatis.executor.statement.StatementHandler;
import org.example.mybatis.mapping.BoundSql;
import org.example.mybatis.mapping.Environment;
import org.example.mybatis.mapping.MapperStatement;
import org.example.mybatis.transaction.Transaction;
import org.example.mybatis.transaction.jdbc.JdbcTransactionFactory;
import org.example.mybatis.type.TypeAliasRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Configuration {

    private Environment environment;

    private MapperRegistry registry = new MapperRegistry(this);

    private Map<String, MapperStatement> mapperStatements = new HashMap<>();

    private TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();

    public Configuration() {
        typeAliasRegistry.register("JDBC", JdbcTransactionFactory.class);
        typeAliasRegistry.register("POOLED", PooledDataSourceFactory.class);
        typeAliasRegistry.register("UNPOOLED", UnPooledDataSourceFactory.class);
    }

    public Executor newExecutor(Transaction transaction) {
        return new SimpleExecutor(this, transaction);
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

    public void addMapperStatement(MapperStatement mapperStatement){
        mapperStatements.put(mapperStatement.getId(), mapperStatement);
    }

    public MapperStatement getMapperStatement(String id){
        return mapperStatements.get(id);
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

    public Map<String, MapperStatement> getMapperStatements() {
        return mapperStatements;
    }

    public void setMapperStatements(Map<String, MapperStatement> mapperStatements) {
        this.mapperStatements = mapperStatements;
    }

    public TypeAliasRegistry getTypeAliasRegistry() {
        return typeAliasRegistry;
    }

    public void setTypeAliasRegistry(TypeAliasRegistry typeAliasRegistry) {
        this.typeAliasRegistry = typeAliasRegistry;
    }

    public ResultSetHandler newResultSetHandler(Executor executor, MapperStatement mapperStatement, BoundSql boundSql) {
        return new DefaultResultSetHandler(executor, mapperStatement, boundSql);
    }

    public StatementHandler newStatementHandler(MapperStatement mapperStatement, BoundSql boundSql, ResultHandler resultHandler, Executor executor, Object parameter) {
        return new PrepareStatementHandler(boundSql, mapperStatement, parameter, executor, resultHandler);

    }
}
