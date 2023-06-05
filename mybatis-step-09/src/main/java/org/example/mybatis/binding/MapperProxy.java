package org.example.mybatis.binding;

import org.example.mybatis.session.SqlSession;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MapperProxy implements InvocationHandler, Serializable {

    private Class<?> mapperInterface;

    private Map<Method, MapperMethod> cacheMethods;

    private SqlSession sqlSession;

    public MapperProxy(Class<?> mapperInterface, Map<Method, MapperMethod> cacheMethods, SqlSession sqlSession) {
        this.mapperInterface = mapperInterface;
        this.cacheMethods = cacheMethods;
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
        MapperMethod mapperMethod = cacheMethods.get(method);
        if (mapperMethod == null){
            mapperMethod = new MapperMethod(sqlSession.getConfiguration(), mapperInterface, method);
            cacheMethods.put(method, mapperMethod);
        }
        return mapperMethod;
    }
}
