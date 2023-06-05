package org.example.mybatis.builder;

import org.example.mybatis.session.Configuration;

public class BaseBuilder {

    protected Configuration configuration;

    public BaseBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
