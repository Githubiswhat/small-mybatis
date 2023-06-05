package org.example.mybatis.session.defaults;

import org.example.mybatis.executor.Executor;
import org.example.mybatis.mapping.BoundSql;
import org.example.mybatis.mapping.MappedStatement;
import org.example.mybatis.session.Configuration;
import org.example.mybatis.session.SqlSession;

import java.util.List;

public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    private Executor executor;

    public DefaultSqlSession(Configuration configuration, Executor executor) {
        this.configuration = configuration;
        this.executor = executor;
    }

    @Override
    public <T> T selectOne(String statement) {
        return selectOne(statement, null);
    }

    @Override
    public <T> T selectOne(String statement, Object args) {
        MappedStatement mappedStatement = configuration.getMappedStatement(statement);
        BoundSql boundSql = mappedStatement.getSqlSource().getBoundSql(statement);
        List<Object> result = executor.query(args, boundSql, mappedStatement);
        return (T) result.get(0);
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        return configuration.getMapper(type, this);
    }
}
