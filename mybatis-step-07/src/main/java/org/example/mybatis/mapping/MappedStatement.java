package org.example.mybatis.mapping;

import org.example.mybatis.session.Configuration;

public class MappedStatement {

    private Configuration configuration;

    private BoundSql boundSql;

    private String id;

    private SqlCommandType sqlCommandType;

    public static class Builder {

        private MappedStatement mappedStatement = new MappedStatement();

        public Builder(Configuration configuration, BoundSql boundSql, String id, SqlCommandType sqlCommandType) {
            mappedStatement.configuration = configuration;
            mappedStatement.boundSql = boundSql;
            mappedStatement.id = id;
            mappedStatement.sqlCommandType = sqlCommandType;
        }

        public MappedStatement build() {
            assert mappedStatement.configuration != null;
            assert mappedStatement.id != null;
            return mappedStatement;
        }

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SqlCommandType getSqlCommandType() {
        return sqlCommandType;
    }

    public void setSqlCommandType(SqlCommandType sqlCommandType) {
        this.sqlCommandType = sqlCommandType;
    }
}
