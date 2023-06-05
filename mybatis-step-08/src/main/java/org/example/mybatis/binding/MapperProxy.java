package org.example.mybatis.binding;

import org.example.mybatis.session.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MapperProxy implements InvocationHandler {

    private Class<?> mapperInterfaces;

    private Map<Method, MapperMethod> methodCache = new HashMap<Method, MapperMethod>();

    private SqlSession session;

    public MapperProxy(Class<?> mapperInterfaces, Map<Method, MapperMethod> methodCache, SqlSession session) {
        this.mapperInterfaces = mapperInterfaces;
        this.methodCache = methodCache;
        this.session = session;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())){
            method.invoke(this, args);
        }
        MapperMethod mapperMethod = cacheMethod(method);
        return mapperMethod.execute(session, args);
    }

    private MapperMethod cacheMethod(Method method) {
        MapperMethod mapperMethod = methodCache.get(method);
        if (mapperMethod == null){
            mapperMethod = new MapperMethod(session.getConfiguration(), mapperInterfaces, method);
            methodCache.put(method, mapperMethod);
        }
        return mapperMethod;
    }
}
