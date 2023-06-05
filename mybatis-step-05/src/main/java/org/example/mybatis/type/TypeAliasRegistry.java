package org.example.mybatis.type;

import java.util.HashMap;
import java.util.Locale;
import java.util.logging.Handler;

public class TypeAliasRegistry {

    private HashMap<String, Class<?>> typeAliases = new HashMap<String, Class<?>>();

    public TypeAliasRegistry() {
        registerAlias("string", String.class);

        registerAlias("byte", Byte.class);
        registerAlias("short", Short.class);
        registerAlias("int", Integer.class);
        registerAlias("integer", Integer.class);
        registerAlias("float", Float.class);
        registerAlias("double", Double.class);
        registerAlias("boolean", Boolean.class);
        registerAlias("char", Character.class);
    }

    public void registerAlias(String alias, Class<?> value){
        String key = alias.toLowerCase(Locale.ENGLISH);
        typeAliases.put(key, value);
    }

    public <T> Class<T> resolveAlias(String alias){
        String key = alias.toLowerCase(Locale.ENGLISH);
        return (Class<T>) typeAliases.get(key);
    }
}
