package org.example.mybatis.binding;

import cn.hutool.core.util.ClassUtil;
import org.example.mybatis.session.Configuration;
import org.example.mybatis.session.SqlSession;

import java.util.HashMap;
import java.util.Set;

public class MapperRegistry {

    private Configuration configuration;

    public MapperRegistry(Configuration configuration) {
        this.configuration = configuration;
    }

    private HashMap<Class<?>, MapperProxyFactory<?>> knownMappers = new HashMap<>();

    public <T> void addMapper(Class<T> type) {
        if (type.isInterface()){
            if (hasType(type)){
                throw new RuntimeException();
            }
            MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory<T>) new MapperProxyFactory<>(type);
            knownMappers.put(type, mapperProxyFactory);
        }
    }

    public  <T> boolean hasType(Class<T> type) {
        return knownMappers.containsKey(type);
    }

    public void addMappers(String packageName){
        Set<Class<?>> mapperClasses = ClassUtil.scanPackage(packageName);
        for (Class<?> mapperClass : mapperClasses) {
            addMapper(mapperClass);
        }
    }

    public <T> T getMapper(Class<T> type, SqlSession sqlSession){
        MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory<T>) knownMappers.get(type);
        if (mapperProxyFactory == null){
            throw new RuntimeException("mapRegistry don't have know that type " + type);
        }
        try {
            return mapperProxyFactory.newInstance(sqlSession);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
