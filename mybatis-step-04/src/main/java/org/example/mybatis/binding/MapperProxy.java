package org.example.mybatis.binding;

import org.example.mybatis.session.SqlSession;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

public class MapperProxy<T> implements Serializable, InvocationHandler {

    private SqlSession sqlSession;

    private Class<T> mapperInterface;

    private Map<Method, MapperMethod> methodCache;

    public MapperProxy(SqlSession sqlSession, Class<T> mapperInterface, Map<Method, MapperMethod> methodCache) {
        this.sqlSession = sqlSession;
        this.mapperInterface = mapperInterface;
        this.methodCache = methodCache;
    }


    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())){
            method.invoke(this, objects);
        }
        MapperMethod mapperMethod = cacheMethod(method);
        return mapperMethod.execute(sqlSession, objects);
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
