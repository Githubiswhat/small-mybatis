package org.example.mybatis.mapping;

import org.example.mybatis.session.Configuration;

public class MapperStatement {

    private String id;

    private Configuration configuration;

    private BoundSql boundSql;

    private SqlCommandType sqlCommandType;

    public MapperStatement() {
    }

    public static class Builder {

        private MapperStatement mapperStatement = new MapperStatement();

        public Builder(String id, Configuration configuration, BoundSql boundSql, SqlCommandType sqlCommandType) {
            mapperStatement.id = id;
            mapperStatement.configuration = configuration;
            mapperStatement.boundSql = boundSql;
            mapperStatement.sqlCommandType = sqlCommandType;
        }

        public MapperStatement build() {
            assert mapperStatement.configuration != null;
            assert mapperStatement.id != null;
            return mapperStatement;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public BoundSql getBoundSql() {
        return boundSql;
    }

    public void setBoundSql(BoundSql boundSql) {
        this.boundSql = boundSql;
    }

    public SqlCommandType getSqlCommandType() {
        return sqlCommandType;
    }

    public void setSqlCommandType(SqlCommandType sqlCommandType) {
        this.sqlCommandType = sqlCommandType;
    }
}
