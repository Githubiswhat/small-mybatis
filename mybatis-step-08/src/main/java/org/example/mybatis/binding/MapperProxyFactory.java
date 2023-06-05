package org.example.mybatis.binding;

import org.example.mybatis.session.SqlSession;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class MapperProxyFactory<T> {

    private Class<?> mapperInterfaces;

    private Map<Method, MapperMethod> methodCache = new HashMap<Method, MapperMethod>();

    public MapperProxyFactory(Class<?> mapperInterfaces) {
        this.mapperInterfaces = mapperInterfaces;
    }

    public T newInstance(SqlSession session){
        MapperProxy mapperProxy = new MapperProxy(mapperInterfaces, methodCache, session);
        return (T) Proxy.newProxyInstance(mapperInterfaces.getClassLoader(), new Class[]{mapperInterfaces}, mapperProxy);
    }

}
