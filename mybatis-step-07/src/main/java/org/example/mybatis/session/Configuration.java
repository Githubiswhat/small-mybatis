package org.example.mybatis.session;

import org.example.mybatis.binding.MapperRegistry;
import org.example.mybatis.datasource.druid.DruidDatasourceFactory;
import org.example.mybatis.datasource.pooled.PooledDataSourceFactory;
import org.example.mybatis.datasource.unpooled.UnpooledDataSourceFactory;
import org.example.mybatis.executor.Executor;
import org.example.mybatis.executor.SimpleExecutor;
import org.example.mybatis.executor.resultSet.DefaultResultSetHandler;
import org.example.mybatis.executor.resultSet.ResultSetHandler;
import org.example.mybatis.executor.statement.PrepareStatementHandler;
import org.example.mybatis.executor.statement.StatementHandler;
import org.example.mybatis.mapping.BoundSql;
import org.example.mybatis.mapping.Environment;
import org.example.mybatis.mapping.MappedStatement;
import org.example.mybatis.transaction.Transaction;
import org.example.mybatis.transaction.jdbc.JdbcTransactionFactory;
import org.example.mybatis.type.TypeAliasRegistry;

import java.util.HashMap;
import java.util.Map;

public class Configuration {

    private Environment environment;

    private MapperRegistry registry = new MapperRegistry(this);

    private Map<String, MappedStatement> mappedStatements = new HashMap<>();

    private TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();

    public Configuration() {
        this.typeAliasRegistry.register("JDBC", JdbcTransactionFactory.class);
        this.typeAliasRegistry.register("DRUID", DruidDatasourceFactory.class);
        this.typeAliasRegistry.register("UNPOOLED", UnpooledDataSourceFactory.class);
        this.typeAliasRegistry.register("POOLED", PooledDataSourceFactory.class);
    }

    public boolean hasMapper(Class<?> type) {
        return registry.hasMapper(type);
    }


    public void addMappers(String packageName){
        registry.addMappers(packageName);
    }


    public <T> void addMapper(Class<T> type){
        registry.addMapper(type);
    }


    public <T> T getMapper(Class<T> type, SqlSession sqlSession){
        return registry.getMapper(type, sqlSession);
    }

    public void addMapperStatement(MappedStatement mappedStatement){
        mappedStatements.put(mappedStatement.getId(), mappedStatement);
    }

    public MappedStatement getMappedStatement(String id){
        return mappedStatements.get(id);
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

    public StatementHandler newStatementHandle(Executor executor, MappedStatement mappedStatement, BoundSql boundSql, ResultHandler resultHandler, Object parameter) {
        return new PrepareStatementHandler(mappedStatement, boundSql, resultHandler, executor, parameter);
    }

    public ResultSetHandler newResultSetHandler(Executor executor, MappedStatement mappedStatement, BoundSql boundSql) {
        return new DefaultResultSetHandler(executor, mappedStatement, boundSql);
    }

    public Executor newExecutor(Transaction tx) {
        return new SimpleExecutor(this, tx);
    }
}
