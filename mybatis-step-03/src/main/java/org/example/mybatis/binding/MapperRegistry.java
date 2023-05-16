package org.example.mybatis.binding;

import cn.hutool.core.util.ClassUtil;
import org.example.mybatis.session.SqlSession;

import java.util.HashMap;
import java.util.Set;

public class MapperRegistry {

    private HashMap<Class<?>, MapperProxyFactory<?>> knowMappers = new HashMap<>();

    public <T> void addMapper(Class<T> type){
        if (type.isInterface()){
            if (hasType(type)){
                throw new RuntimeException("Type: " + type + " is already known by the mapperRegister.");
            }
            knowMappers.put(type, new MapperProxyFactory<>(type));
        }
    }

    private <T> boolean hasType(Class<T> type) {
        return knowMappers.containsKey(type);
    }

    public <T> T getMapper(Class<T> type, SqlSession sqlSession){
        final MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory<T>) knowMappers.get(type);
        if (mapperProxyFactory == null){
            throw new RuntimeException("Type " + type + " is not know by the mapperRegister");
        }
        try {
            return mapperProxyFactory.newInstance(sqlSession);
        } catch (Exception e) {
            throw new RuntimeException("Error getting mapper instance ", e);
        }
    }


    public void addMappers(String packageName){
        Set<Class<?>> mapperSet = ClassUtil.scanPackage(packageName);
        for (Class<?> mapperClass : mapperSet){
            addMapper(mapperClass);
        }
    }

}
