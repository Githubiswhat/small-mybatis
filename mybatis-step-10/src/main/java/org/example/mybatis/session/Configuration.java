package org.example.mybatis.session;

import org.example.mybatis.binding.MapperRegistry;
import org.example.mybatis.datasource.druid.DruidDataSourceFactory;
import org.example.mybatis.datasource.pooled.PooledDataSourceFactory;
import org.example.mybatis.datasource.unpooled.UnPooledDataSourceFactory;
import org.example.mybatis.executor.Executor;
import org.example.mybatis.executor.SimpleExecutor;
import org.example.mybatis.executor.parameter.ParameterHandler;
import org.example.mybatis.executor.resultset.DefaultResultSetHandler;
import org.example.mybatis.executor.resultset.ResultSetHandler;
import org.example.mybatis.executor.statement.PrepareStatementHandler;
import org.example.mybatis.executor.statement.StatementHandler;
import org.example.mybatis.mapping.BoundSql;
import org.example.mybatis.mapping.Environment;
import org.example.mybatis.mapping.MappedStatement;
import org.example.mybatis.reflection.MetaObject;
import org.example.mybatis.reflection.facotry.DefaultObjectFactory;
import org.example.mybatis.reflection.facotry.ObjectFactory;
import org.example.mybatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.example.mybatis.reflection.wrapper.ObjectWrapperFactory;
import org.example.mybatis.scripting.LanguageDriver;
import org.example.mybatis.scripting.LanguageDriverRegistry;
import org.example.mybatis.scripting.xmltags.XMLLanguageDriver;
import org.example.mybatis.transaction.Transaction;
import org.example.mybatis.transaction.jdbc.JdbcTransactionFactory;
import org.example.mybatis.type.TypeAliasRegistry;
import org.example.mybatis.type.TypeHandlerRegistry;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Configuration {

    private Environment environment;

    private MapperRegistry mapperRegistry = new MapperRegistry(this);

    private Map<String, MappedStatement> mappedStatements = new HashMap<String, MappedStatement>();

    // 类型别名注册机
    protected final TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();

    // 类型处理器注册机
    protected final TypeHandlerRegistry typeHandlerRegistry = new TypeHandlerRegistry();

    protected final LanguageDriverRegistry languageRegistry = new LanguageDriverRegistry();


    // 对象工厂和对象包装器工厂
    protected ObjectFactory objectFactory = new DefaultObjectFactory();
    protected ObjectWrapperFactory objectWrapperFactory = new DefaultObjectWrapperFactory();

    protected final Set<String> loadedResources = new HashSet<>();

    protected String databaseId;

    public Configuration() {
        typeAliasRegistry.registerAlias("JDBC", JdbcTransactionFactory.class);

        typeAliasRegistry.registerAlias("DRUID", DruidDataSourceFactory.class);
        typeAliasRegistry.registerAlias("UNPOOLED", UnPooledDataSourceFactory.class);
        typeAliasRegistry.registerAlias("POOLED", PooledDataSourceFactory.class);

        languageRegistry.setDefaultDriverClass(XMLLanguageDriver.class);
    }

    public void addMappers(String packageName) {
        mapperRegistry.addMappers(packageName);
    }

    public void addMapper(Class<?> type) {
        mapperRegistry.addMapper(type);
    }

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return mapperRegistry.getMapper(type, sqlSession);
    }

    public boolean hasMapper(Class<?> type) {
        return mapperRegistry.hasMapper(type);
    }

    public void addMappedStatement(MappedStatement mappedStatement){
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

    public TypeHandlerRegistry getTypeHandlerRegistry() {
        return typeHandlerRegistry;
    }

    public LanguageDriverRegistry getLanguageRegistry() {
        return languageRegistry;
    }

    public ObjectFactory getObjectFactory() {
        return objectFactory;
    }

    public void setObjectFactory(ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
    }

    public ObjectWrapperFactory getObjectWrapperFactory() {
        return objectWrapperFactory;
    }

    public void setObjectWrapperFactory(ObjectWrapperFactory objectWrapperFactory) {
        this.objectWrapperFactory = objectWrapperFactory;
    }

    public Set<String> getLoadedResources() {
        return loadedResources;
    }

    public String getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(String databaseId) {
        this.databaseId = databaseId;
    }

    public boolean isResourceLoad(String resource) {
        return loadedResources.contains(resource);
    }

    public void addLoadResource(String resource) {
        loadedResources.add(resource);
    }

    /**
     * 创建结果集处理器
     */
    public ResultSetHandler newResultSetHandler(Executor executor, MappedStatement mappedStatement, BoundSql boundSql) {
        return new DefaultResultSetHandler(executor, mappedStatement, boundSql);
    }

    /**
     * 生产执行器
     */
    public Executor newExecutor(Transaction transaction) {
        return new SimpleExecutor(this, transaction);
    }

    /**
     * 创建语句处理器
     */
    public StatementHandler newStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameter, ResultHandler resultHandler, BoundSql boundSql) {
        return new PrepareStatementHandler(executor, mappedStatement, parameter, resultHandler, boundSql);
    }

    // 创建元对象
    public MetaObject newMetaObject(Object object) {
        return MetaObject.forObject(object, objectFactory, objectWrapperFactory);
    }

    public ParameterHandler newParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
        // 创建参数处理器
        ParameterHandler parameterHandler = mappedStatement.getLang().createParameterHandler(mappedStatement, parameterObject, boundSql);
        // 插件的一些参数，也是在这里处理，暂时不添加这部分内容 interceptorChain.pluginAll(parameterHandler);
        return parameterHandler;
    }

    public LanguageDriver getDefaultScriptingLanguageInstance() {
        return languageRegistry.getDefaultDriver();
    }

}
