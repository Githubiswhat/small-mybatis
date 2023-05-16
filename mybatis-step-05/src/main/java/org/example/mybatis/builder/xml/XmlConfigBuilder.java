package org.example.mybatis.builder.xml;

import cn.hutool.core.bean.BeanUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.example.mybatis.Mapping.MappedStatement;
import org.example.mybatis.Mapping.SqlCommandType;
import org.example.mybatis.builder.BaseBuilder;
import org.example.mybatis.io.Resources;
import org.example.mybatis.session.Configuration;
import org.xml.sax.InputSource;

import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlConfigBuilder extends BaseBuilder {

    private final Element root;

    public XmlConfigBuilder(Reader reader) {
        super(new Configuration());
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(reader);
            root = document.getRootElement();
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }

    public Configuration parse(){
        try {
            mapperElement(root.element("mappers"));
        } catch (DocumentException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return configuration;
    }

    private void mapperElement(Element mappers) throws DocumentException, ClassNotFoundException {
        List<Element> mapper = mappers.elements("mapper");
        for (Element e : mapper) {
            String resource = e.attributeValue("resource");
            SAXReader saxReader = new SAXReader();
            Reader reader = Resources.getResourceAsReader(resource);
            Document document = saxReader.read(new InputSource(reader));
            Element root = document.getRootElement();

            String namespace = root.attributeValue("namespace");
            List<Element> selectNodes = root.elements("select");
            for (Element node : selectNodes) {
                String id = node.attributeValue("id");
                String parameterType = node.attributeValue("parameterType");
                String resultType = node.attributeValue("resultType");
                String sql = node.getText();
                HashMap<Integer, String> parameter = new HashMap<>();

                Pattern pattern = Pattern.compile("(#\\{(.*?)\\})");
                Matcher matcher = pattern.matcher(sql);
                for (int i = 1; matcher.find(); i++) {
                    String g1 = matcher.group(1);
                    String g2 = matcher.group(2);
                    parameter.put(i, g2);
                    sql = sql.replace(g1,  "?");
                }

                SqlCommandType sqlCommandType = SqlCommandType.valueOf(node.getName().toUpperCase());

                String mId = namespace + "." + id;

                MappedStatement mappedStatement = new MappedStatement.Builder(configuration, sqlCommandType, mId, sql, resultType, parameterType, parameter).build();

                configuration.addMappedStatement(mappedStatement);
            }

            configuration.addMapper(Resources.classForName(namespace));
        }
    }
}
