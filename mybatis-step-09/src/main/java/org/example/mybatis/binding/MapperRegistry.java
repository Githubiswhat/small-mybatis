package org.example.mybatis.binding;

import cn.hutool.core.util.ClassUtil;
import org.example.mybatis.session.Configuration;
import org.example.mybatis.session.SqlSession;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MapperRegistry{

    private Configuration configuration;

    public MapperRegistry(Configuration configuration) {
        this.configuration = configuration;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    private Map<Class<?>, MapperProxyFactory<?>> knownMappers = new HashMap<>();


    public void addMappers(String packageName){
        Set<Class<?>> classes = ClassUtil.scanPackage(packageName);
        for (Class<?> mapperClass : classes) {
            addMapper(mapperClass);
        }
    }

    public void addMapper(Class<?> type){
        if (type.isInterface()){
            if (hasMapper(type)){
                throw new RuntimeException("the type '" + type.getName() + "' already existed");
            }
            MapperProxyFactory<Object> mapperProxyFactory = new MapperProxyFactory<>(type);
            knownMappers.put(type, mapperProxyFactory);
        }
    }

    public boolean hasMapper(Class<?> type) {
        return knownMappers.containsKey(type);
    }

    public <T> T getMapper(Class<T> type, SqlSession session) {
        MapperProxyFactory<?> mapperProxyFactory = knownMappers.get(type);
        if (mapperProxyFactory == null) {
            throw new RuntimeException("don't hava the type'" + type + "' mapper");
        }
        try {
            return (T) mapperProxyFactory.newInstance(session);
        } catch (Exception e) {
            throw new RuntimeException("instantiate mapper '" + type.getName() + "' failed");
        }
    }

}
