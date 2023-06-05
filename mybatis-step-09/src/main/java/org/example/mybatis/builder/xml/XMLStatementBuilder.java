package org.example.mybatis.builder.xml;

import org.dom4j.Element;
import org.example.mybatis.builder.BaseBuilder;
import org.example.mybatis.mapping.BoundSql;
import org.example.mybatis.mapping.MappedStatement;
import org.example.mybatis.mapping.SqlCommandType;
import org.example.mybatis.mapping.SqlSource;
import org.example.mybatis.scripting.LanguageDriver;
import org.example.mybatis.session.Configuration;

import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XMLStatementBuilder extends BaseBuilder {

    private String currentNamespace;
    private Element element;

    public XMLStatementBuilder(Configuration configuration, Element element, String currentNamespace) {
        super(configuration);
        this.element = element;
        this.currentNamespace = currentNamespace;
    }

    public void parseStatementNode() {
        String id = element.attributeValue("id");
        // 参数类型
        String parameterType = element.attributeValue("parameterType");
        Class<?> parameterTypeClass = resolveAlias(parameterType);
        // 结果类型
        String resultType = element.attributeValue("resultType");
        Class<?> resultTypeClass = resolveAlias(resultType);
        // 获取命令类型(select|insert|update|delete)
        String nodeName = element.getName();
        SqlCommandType sqlCommandType = SqlCommandType.valueOf(nodeName.toUpperCase(Locale.ENGLISH));

        // 获取默认语言驱动器
        Class<?> langClass = configuration.getLanguageRegistry().getDefaultDriverClass();
        LanguageDriver langDriver = configuration.getLanguageRegistry().getDriver(langClass);

        SqlSource sqlSource = langDriver.createSqlSource(configuration, element, parameterTypeClass);

        MappedStatement mappedStatement = new MappedStatement.Builder(configuration, currentNamespace + "." + id, sqlCommandType, sqlSource, resultTypeClass).build();

        // 添加解析 SQL
        configuration.addMappedStatement(mappedStatement);
    }
}
