package org.example.mybatis.binding;

import cn.hutool.core.util.ClassUtil;
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
        for (Class<?> clazz : classes) {
            addMapper(clazz);
        }
    }

    public void addMapper(Class<?> clazz){
        if (clazz.isInterface()){
            if (! hasMapper(clazz)){
                MapperProxyFactory mapperProxyFactory = new MapperProxyFactory<>(clazz);
                knownMappers.put(clazz, mapperProxyFactory);
            }
        }
    }

    public boolean hasMapper(Class<?> clazz) {
        return knownMappers.containsKey(clazz);
    }


    public <T> T getMapper(Class<T> clazz, SqlSession sqlSession){
        MapperProxyFactory<?> mapperProxyFactory = knownMappers.get(clazz);
        if (mapperProxyFactory == null){
            throw new RuntimeException("don't have such type:" + clazz + " mapper");
        }
        Object proxy = mapperProxyFactory.newInstance(sqlSession);
        return (T) proxy;
    }

}
