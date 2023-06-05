package org.example.mybatis.binding;

import org.example.mybatis.session.SqlSession;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class MapperProxyFactory<T> {

    private Class<?> mapperInterface;

    private Map<Method, MapperMethod> cacheMethods = new HashMap<>();

    public MapperProxyFactory(Class<?> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    public Object newInstance(SqlSession session){
        MapperProxy mapperProxy = new MapperProxy(mapperInterface, cacheMethods, session);
        return Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[]{mapperInterface}, mapperProxy);
    }
}
