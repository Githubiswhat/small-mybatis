package org.example.mybatis.mapping;

import org.example.mybatis.scripting.LanguageDriver;
import org.example.mybatis.session.Configuration;

public class MappedStatement {

    private String id;

    private SqlCommandType sqlCommandType;

    private Configuration configuration;

    private SqlSource sqlSource;

    Class<?> resultType;

    private LanguageDriver lang;

    public MappedStatement() {
    }

    public static class Builder{

        private MappedStatement mappedStatement = new MappedStatement();

        public Builder(Configuration configuration, String id, SqlCommandType sqlCommandType, SqlSource sqlSource, Class<?> resultType) {
            mappedStatement.configuration = configuration;
            mappedStatement.id = id;
            mappedStatement.sqlCommandType = sqlCommandType;
            mappedStatement.sqlSource = sqlSource;
            mappedStatement.resultType = resultType;
            mappedStatement.lang = configuration.getDefaultScriptingLanguageInstance();
        }


        public MappedStatement build() {
            assert mappedStatement.id != null;
            assert mappedStatement.configuration != null;
            return mappedStatement;
        }

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

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public SqlSource getSqlSource() {
        return sqlSource;
    }

    public void setSqlSource(SqlSource sqlSource) {
        this.sqlSource = sqlSource;
    }

    public Class<?> getResultType() {
        return resultType;
    }

    public void setResultType(Class<?> resultType) {
        this.resultType = resultType;
    }

    public LanguageDriver getLang() {
        return lang;
    }

    public void setLang(LanguageDriver lang) {
        this.lang = lang;
    }
}
