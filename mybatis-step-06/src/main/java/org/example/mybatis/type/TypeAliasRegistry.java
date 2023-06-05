package org.example.mybatis.type;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TypeAliasRegistry {

    private Map<String, Class<?>> typeAliasRegistry = new HashMap<String, Class<?>>();

    public TypeAliasRegistry() {
        registerAlias("string", String.class);

        registerAlias("byte", Byte.class);
        registerAlias("short", Short.class);
        registerAlias("int", Integer.class);
        registerAlias("integer", Integer.class);
        registerAlias("long", Long.class);
        registerAlias("float", Float.class);
        registerAlias("double", Double.class);
        registerAlias("boolean", Boolean.class);
        registerAlias("char", Character.class);
    }

    public void registerAlias(String alias, Class<?> clazz) {
        alias = alias.toUpperCase(Locale.ENGLISH);
        typeAliasRegistry.put(alias, clazz);
    }

    public Class<?> resolveAlias(String alias){
        alias = alias.toUpperCase(Locale.ENGLISH);
        return typeAliasRegistry.get(alias);
    }

}
