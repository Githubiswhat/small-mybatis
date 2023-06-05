package org.example.mybatis.mapping;

import org.example.mybatis.session.Configuration;

public class MappedStatement {

    private Configuration configuration;

    private String id;

    private BoundSql boundSql;

    private SqlCommandType sqlCommandType;

    public MappedStatement() {
    }

    public static class Builder{

        private MappedStatement mappedStatement = new MappedStatement();

        public Builder(Configuration configuration, String id, BoundSql boundSql, SqlCommandType sqlCommandType) {
            mappedStatement.configuration = configuration;
            mappedStatement.id = id;
            mappedStatement.boundSql = boundSql;
            mappedStatement.sqlCommandType = sqlCommandType;
        }

        public MappedStatement build() {
            assert mappedStatement.id != null;
            assert mappedStatement.configuration != null;
            return mappedStatement;
        }
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
