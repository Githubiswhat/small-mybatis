package org.example.mybatis.session.defaults;

import org.example.mybatis.binding.MapperRegistry;
import org.example.mybatis.session.SqlSession;

public class DefaultSqlSession implements SqlSession {

    private MapperRegistry mapperRegister;

    public DefaultSqlSession(MapperRegistry mapperRegister) {
        this.mapperRegister = mapperRegister;
    }

    @Override
    public <T> T selectOne(String statement) {
        return (T) ("你的操作被代理了！" + statement);
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        return (T) ("你的操作被代理了！" + "方法："  + statement + "入参：" + parameter);
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        return mapperRegister.getMapper(type, this);
    }
}
