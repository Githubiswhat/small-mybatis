package org.example.mybatis.builder.xml;


import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.example.mybatis.builder.BaseBuilder;
import org.example.mybatis.io.Resources;
import org.example.mybatis.session.Configuration;

import java.io.InputStream;
import java.util.List;

public class XMLMapperBuilder extends BaseBuilder {

    private Element element;

    private String resource;

    private String currentNamespace;


    public XMLMapperBuilder(InputStream inputStream, Configuration configuration, String resource) throws DocumentException {
        this(new SAXReader().read(inputStream), configuration, resource);
    }

    public XMLMapperBuilder(Document document, Configuration configuration, String resource) {
        super(configuration);
        this.element = document.getRootElement();
        this.resource = resource;
    }

    public void parse() throws Exception{
        if (!configuration.isResourceLoad(resource)){
            configurationElement(element);
            configuration.addLoadResource(resource);
            configuration.addMapper(Resources.classForName(currentNamespace));
        }
    }

    private void configurationElement(Element element) {
        currentNamespace = element.attributeValue("namespace");
        if (currentNamespace.equals("")){
            throw new RuntimeException("Mapper's namespace cannot be empty");
        }

        buildStatementFromContext(element.elements("select"));
    }

    private void buildStatementFromContext(List<Element> list) {
        for (Element element : list) {
            XMLStatementBuilder xmlStatementBuilder = new XMLStatementBuilder(configuration, currentNamespace, element);
            xmlStatementBuilder.parseStatementNode();
        }
    }
}
