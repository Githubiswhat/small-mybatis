package org.example.mybatis.binding;

import cn.hutool.core.util.ClassUtil;
import org.example.mybatis.io.Resources;
import org.example.mybatis.session.Configuration;
import org.example.mybatis.session.SqlSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MapperRegistry {

    private Configuration configuration;

    public MapperRegistry(Configuration configuration) {
        this.configuration = configuration;
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
            if (! hasMapper(type)){
                MapperProxyFactory<?> mapperProxyFactory = new MapperProxyFactory<>(type);
                knownMappers.put(type, mapperProxyFactory);
            }
        }
    }

    public boolean hasMapper(Class<?> type) {
        return knownMappers.containsKey(type);
    }

    public <T> T getMapper(Class<?> type, SqlSession session){
        MapperProxyFactory<?> mapperProxyFactory = knownMappers.get(type);
        if (mapperProxyFactory == null){
            throw new RuntimeException("the type: " + type + "don't know by the mapper Registry");
        }
        try {
            return (T) mapperProxyFactory.newInstance(session);
        } catch (Exception e) {
            throw new RuntimeException("Error get mapper instance: " + type);
        }
    }

}
