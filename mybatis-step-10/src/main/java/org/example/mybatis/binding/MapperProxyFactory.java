package org.example.mybatis.binding;

import org.example.mybatis.session.SqlSession;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class MapperProxyFactory<T> {

    private Class<T> mapperInterface;

    private Map<Method, MapperMethod> methodCache = new HashMap<>();

    public MapperProxyFactory(Class<T> type) {
        this.mapperInterface = type;
    }

    public T newInstance(SqlSession sqlSession){
        MapperProxy mapperProxy = new MapperProxy(mapperInterface, sqlSession, methodCache);
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[]{mapperInterface}, mapperProxy);
    }


}
