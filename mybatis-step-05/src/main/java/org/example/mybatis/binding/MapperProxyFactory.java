package org.example.mybatis.binding;

import org.example.mybatis.session.SqlSession;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class MapperProxyFactory<T> {

    private Class<T> methodInterface;

    private Map<Method, MapperMethod> methodCache = new HashMap<>();

    public MapperProxyFactory(Class<T> methodInterface) {
        this.methodInterface = methodInterface;
    }

    public Map<Method, MapperMethod> getMethodCache() {
        return methodCache;
    }

    public T newInstance(SqlSession session){
        MapperProxy mapperProxy = new MapperProxy(methodInterface, session, methodCache);
        return (T) Proxy.newProxyInstance(methodInterface.getClassLoader(), new Class[]{methodInterface}, mapperProxy);
    }
}
