package org.example.mybatis.builder;

import org.example.mybatis.session.Configuration;
import org.example.mybatis.type.TypeAliasRegistry;
import org.example.mybatis.type.TypeHandlerRegistry;

public abstract class BaseBuilder {

    protected Configuration configuration;

    protected TypeAliasRegistry typeAliasRegistry;

    protected TypeHandlerRegistry typeHandlerRegistry;

    public BaseBuilder(Configuration configuration) {
        this.configuration = configuration;
        this.typeAliasRegistry = configuration.getTypeAliasRegistry();
        this.typeHandlerRegistry = configuration.getTypeHandlerRegistry();
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    protected Class<?> resolveAlias(String alias) {
        return typeAliasRegistry.resolveAlias(alias);
    }
}
