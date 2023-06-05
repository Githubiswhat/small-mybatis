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

    private Map<Method, MapperMethod> methodCache;

    public MapperProxy(Class<T> mapperInterface, SqlSession sqlSession, Map<Method, MapperMethod> methodCache) {
        this.mapperInterface = mapperInterface;
        this.sqlSession = sqlSession;
        this.methodCache = methodCache;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())){
            return method.invoke(this, args);
        }else {
            MapperMethod mapperMethod = cacheMethod(method);
            return mapperMethod.execute(sqlSession, args);
        }
    }

    private MapperMethod cacheMethod(Method method) {
        MapperMethod mapperMethod = methodCache.get(method);
        if (mapperMethod == null){
            mapperMethod = new MapperMethod(sqlSession.getConfiguration(), method, mapperInterface);
            methodCache.put(method, mapperMethod);
        }
        return mapperMethod;
    }
}
