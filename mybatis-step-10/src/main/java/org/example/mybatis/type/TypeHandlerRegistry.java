package org.example.mybatis.type;

import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class TypeHandlerRegistry {

    private final Map<JdbcType, TypeHandler<?>> JDBC_TYPE_HANDLE_MAP = new EnumMap<>(JdbcType.class);

    private final Map<Type, Map<JdbcType, TypeHandler<?>>> TYPE_HANDLE_MAP = new HashMap<>();

    private final Map<Class<?>, TypeHandler<?>> ALL_TYPE_HANDLERS_MAP = new HashMap<>();

    public TypeHandlerRegistry() {
        register(Long.class, new LongTypeHandler());
        register(long.class, new LongTypeHandler());

        register(String.class, new StringTypeHandler());
        register(String.class, JdbcType.CHAR, new StringTypeHandler());
        register(String.class, JdbcType.VARCHAR, new StringTypeHandler());
    }

    private <T> void register(Type javaType, TypeHandler<? extends T> typeHandler){
        register(javaType, null, typeHandler);
    }

    private void register(Type javaType, JdbcType jdbcType, TypeHandler<?> handler) {
        if (null != javaType){
            Map<JdbcType, TypeHandler<?>> map = TYPE_HANDLE_MAP.computeIfAbsent(javaType, key -> new HashMap<>());
            map.put(jdbcType, handler);
        }
        ALL_TYPE_HANDLERS_MAP.put(handler.getClass(), handler);
    }

    public boolean hasTypeHandler(Class<?> javaType) {
        return hasTypeHandler(javaType, null);
    }

    private boolean hasTypeHandler(Class<?> javaType, JdbcType jdbcType) {
        return javaType != null && getTypeHandler(((Type) javaType), jdbcType) != null;
    }

    private <T> TypeHandler<T> getTypeHandler(Type javaType, JdbcType jdbcType) {
        Map<JdbcType, TypeHandler<?>> jdbcHandlerMap = TYPE_HANDLE_MAP.get(javaType);
        TypeHandler<?> handler = null;
        if (jdbcHandlerMap != null){
            handler = jdbcHandlerMap.get(jdbcHandlerMap);
            if (handler == null){
                handler = jdbcHandlerMap.get(null);
            }
        }
        return (TypeHandler<T>) handler;
    }

    public <T> TypeHandler<T> getTypeHandler(Class<T> type, JdbcType jdbcType){
        return getTypeHandler((Type) type, jdbcType);
    }

}


