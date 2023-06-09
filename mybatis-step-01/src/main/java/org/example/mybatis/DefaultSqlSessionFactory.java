package org.example.mybatis;

public class DefaultSqlSessionFactory implements SqlSessionFactory{

    private Configuration configuration;


    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(configuration.getConnection(), configuration.getMapperElement());
    }
}
