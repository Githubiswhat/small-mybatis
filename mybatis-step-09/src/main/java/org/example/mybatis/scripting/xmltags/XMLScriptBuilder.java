package org.example.mybatis.scripting.xmltags;

import org.dom4j.Element;
import org.example.mybatis.builder.BaseBuilder;
import org.example.mybatis.mapping.SqlSource;
import org.example.mybatis.scripting.defaults.RawSqlSource;
import org.example.mybatis.session.Configuration;

import java.util.ArrayList;
import java.util.List;

public class XMLScriptBuilder extends BaseBuilder {

    private Element element;

    private boolean isDynamic;

    private Class<?> parameterType;

    public XMLScriptBuilder(Element element, Configuration configuration, Class<?> parameterType) {
        super(configuration);
        this.parameterType = parameterType;
        this.element = element;
    }


    public SqlSource parseScriptNode() {
        List<SqlNode> contents = parseDynamicTags(element);
        MixedSqlNode rootSqlNode = new MixedSqlNode(contents);
        return new RawSqlSource(configuration, rootSqlNode, parameterType);
    }

    private List<SqlNode> parseDynamicTags(Element element) {
        ArrayList<SqlNode> contents = new ArrayList<SqlNode>();
        String data = element.getText();
        contents.add(new StaticTexSqlNode(data));
        return contents;
    }

}
