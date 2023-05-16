package org.example.mybatis.session.defaults;

import org.example.mybatis.session.Configuration;
import org.example.mybatis.session.SqlSession;
import org.example.mybatis.session.SqlSessionFactory;

public class DefaultSqlSessionfactory implements SqlSessionFactory {

    private Configuration configuration;

    public DefaultSqlSessionfactory(Configuration configuration) {
        this.configuration = configuration;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(configuration);
    }
}
