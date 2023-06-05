package org.example.mybatis.binding;

import org.example.mybatis.session.SqlSession;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MapperProxy<T> implements Serializable, InvocationHandler {

    private Class<T> mapperInterface;

    private SqlSession sqlSession;

    private Map<Method, MapperMethod> methodCache = new HashMap<>();

    public MapperProxy(SqlSession sqlSession, Class<T> mapperInterface, Map<Method, MapperMethod> methodCache) {
        this.mapperInterface = mapperInterface;
        this.methodCache = methodCache;
        this.sqlSession = sqlSession;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())){
            method.invoke(this, args);
        }
        MapperMethod mapperMethod = cacheMethod(method);
        return mapperMethod.execute(sqlSession, args);
    }

    private MapperMethod cacheMethod(Method method) {
        MapperMethod mapperMethod = methodCache.get(method);
        if (mapperMethod == null){
            mapperMethod = new MapperMethod(sqlSession.getConfiguration(), mapperInterface, method);
            methodCache.put(method, mapperMethod);
        }
        return mapperMethod;
    }
}
