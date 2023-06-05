package org.example.mybatis.scripting.xmltags;

import org.dom4j.Element;
import org.example.mybatis.mapping.SqlSource;
import org.example.mybatis.scripting.LanguageDriver;
import org.example.mybatis.session.Configuration;

public class XMLLanguageDriver implements LanguageDriver {

    @Override
    public SqlSource createSqlSource(Configuration configuration, Element script, Class<?> parameterType) {
        XMLScriptBuilder xmlScriptBuilder = new XMLScriptBuilder(script, configuration, parameterType);
        return xmlScriptBuilder.parseScriptNode();
    }
}
