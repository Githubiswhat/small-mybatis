package org.example.mybatis.builder;

import org.example.mybatis.session.Configuration;
import org.example.mybatis.type.TypeAliasRegistry;

public class BaseBuilder {

    protected Configuration configuration;
    private TypeAliasRegistry typeAliasRegistry;

    public BaseBuilder(Configuration configuration) {
        this.configuration = configuration;
        this.typeAliasRegistry = configuration.getTypeAliasRegistry();
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    protected Class<?> resolveAlias(String alias) {
        return typeAliasRegistry.resolve(alias);
    }
}
