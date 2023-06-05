package org.example.mybatis.scripting.xmltags;

import org.dom4j.Element;
import org.example.mybatis.executor.parameter.ParameterHandler;
import org.example.mybatis.mapping.BoundSql;
import org.example.mybatis.mapping.MappedStatement;
import org.example.mybatis.mapping.SqlSource;
import org.example.mybatis.scripting.LanguageDriver;
import org.example.mybatis.scripting.defaults.DefaultParameterHandler;
import org.example.mybatis.session.Configuration;

public class XMLLanguageDriver implements LanguageDriver {
    @Override
    public SqlSource createSqlSource(Configuration configuration, Element script, Class<?> parameterType) {
        XMLScriptBuilder xmlScriptBuilder = new XMLScriptBuilder(configuration, script, parameterType);
        return xmlScriptBuilder.parseScriptNode();
    }

    @Override
    public ParameterHandler createParameterHandler(MappedStatement mappedStatement, Object parameterObject, BoundSql boundSql) {
        return new DefaultParameterHandler(mappedStatement, parameterObject, boundSql);
    }
}
