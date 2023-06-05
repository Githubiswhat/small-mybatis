package org.example.mybatis.binding;

import cn.hutool.core.util.ClassUtil;
import org.example.mybatis.session.Configuration;
import org.example.mybatis.session.SqlSession;
import org.example.mybatis.session.defaults.DefaultSqlSession;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MapperRegistry {

    private Configuration configuration;

    private Map<Class<?>, MapperProxyFactory<?>> knownMappers = new HashMap<>();

    public MapperRegistry(Configuration configuration) {
        this.configuration = configuration;
    }

    public void addMappers(String packageName){
        Set<Class<?>> classes = ClassUtil.scanPackage(packageName);
        for (Class<?> mapperClass : classes) {
            addMapper(mapperClass);
        }
    }

    public void addMapper(Class<?> type) {
        if (type.isInterface()){
            if (hasMapper(type)) {
                throw new RuntimeException(type.getName() + " already known. ");
            }
            MapperProxyFactory mapperProxyFactory = new MapperProxyFactory(type);
            knownMappers.put(type, mapperProxyFactory);
        }
    }

    public boolean hasMapper(Class<?> type) {
        return knownMappers.containsKey(type);
    }

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        final MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory<T>) knownMappers.get(type);
        if (mapperProxyFactory == null) {
            throw new RuntimeException("Type " + type + " is not known to the MapperRegistry.");
        }
        try {
            return mapperProxyFactory.newInstance(sqlSession);
        } catch (Exception e) {
            throw new RuntimeException("Error getting mapper instance. Cause: " + e, e);
        }
    }


    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

}
