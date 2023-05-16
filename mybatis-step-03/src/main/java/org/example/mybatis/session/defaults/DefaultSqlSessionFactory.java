package org.example.mybatis.session.defaults;

import org.example.mybatis.binding.MapperRegistry;
import org.example.mybatis.session.SqlSession;
import org.example.mybatis.session.SqlSessionFactory;

public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private final MapperRegistry mapperRegister;

    public DefaultSqlSessionFactory(MapperRegistry mapperRegister) {
        this.mapperRegister = mapperRegister;
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(mapperRegister);
    }
}
