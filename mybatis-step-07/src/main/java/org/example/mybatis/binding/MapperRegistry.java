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

    private Map<Class<?>, MapperProxyFactory<?>> knownMappers = new HashMap<Class<?>, MapperProxyFactory<?>>();

    public <T> void addMapper(Class<T> type){
        if (type.isInterface()){
            if (!hasMapper(type)){
                MapperProxyFactory<T> mapperProxyFactory = new MapperProxyFactory<>(type);
                knownMappers.put(type, mapperProxyFactory);
            }
        }
    }

    public boolean hasMapper(Class<?> type) {
        return knownMappers.containsKey(type);
    }

    public void addMappers(String packageName){
        Set<Class<?>> classes = ClassUtil.scanPackage(packageName);
        for (Class<?> mapperClass : classes) {
            addMapper(mapperClass);
        }
    }


    public <T> T getMapper(Class<T> type, SqlSession sqlSession){
        MapperProxyFactory<?> mapperProxyFactory = knownMappers.get(type);
        if (mapperProxyFactory == null){
            throw new RuntimeException("no such type:" + type + "mapper");
        }
        return (T) mapperProxyFactory.newInstance(sqlSession);
    }

}
