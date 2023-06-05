package org.example.mybatis.type;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TypeAliasRegistry {

    private Map<String, Class<?>> typeAliasRegistry = new HashMap<String, Class<?>>();

    public TypeAliasRegistry() {
        this.register("string", String.class);

        this.register("byte", Byte.class);
        this.register("short", Short.class);
        this.register("int", Integer.class);
        this.register("integer", Integer.class);
        this.register("long", Long.class);
        this.register("float", Float.class);
        this.register("double", Double.class);
        this.register("char", Character.class);
        this.register("boolean", Boolean.class);
    }

    public void register(String alias, Class<?> type) {
        alias = alias.toLowerCase(Locale.ENGLISH);
        typeAliasRegistry.put(alias, type);
    }

    public Class<?> resolve(String alias){
        alias = alias.toLowerCase(Locale.ENGLISH);
        return typeAliasRegistry.get(alias);
    }

}
