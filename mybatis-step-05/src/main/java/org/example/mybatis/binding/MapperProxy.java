package org.example.mybatis.binding;

import org.example.mybatis.session.SqlSession;

import java.awt.*;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

public class MapperProxy<T> implements Serializable, InvocationHandler {

    private Class<T> methodInterface;
    private SqlSession session;
    private Map<Method, MapperMethod> methodCache;

    public MapperProxy(Class<T> methodInterface, SqlSession session, Map<Method, MapperMethod> methodCache) {
        this.methodInterface = methodInterface;
        this.session = session;
        this.methodCache = methodCache;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            method.invoke(this, objects);
        }
        MapperMethod mapperMethod = cacheMethod(method);
        return mapperMethod.execute(session, objects);
    }

    private MapperMethod cacheMethod(Method method) {
        MapperMethod mapperMethod = methodCache.get(method);
        if (mapperMethod == null){
            mapperMethod = new MapperMethod(session.getConfiguration(), method, methodInterface);
            methodCache.put(method, mapperMethod);
        }
        return mapperMethod;
    }
}
