package org.example.mybatis.builder.xml;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.example.mybatis.builder.BaseBuilder;
import org.example.mybatis.datasource.DataSourceFactory;
import org.example.mybatis.io.Resources;
import org.example.mybatis.mapping.BoundSql;
import org.example.mybatis.mapping.Environment;
import org.example.mybatis.mapping.MappedStatement;
import org.example.mybatis.mapping.SqlCommandType;
import org.example.mybatis.session.Configuration;
import org.example.mybatis.transaction.TransactionFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XMLConfigBuilder extends BaseBuilder {

    private Element root;

    public XMLConfigBuilder(Reader reader){
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
            environmentElement(root.element("environments"));
            mapperElement(root.element("mappers"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return configuration;
    }

    private void mapperElement(Element mappers) throws Exception {
        List<Element> mapperList = mappers.elements("mapper");
        for (Element e : mapperList) {
            String resource = e.attributeValue("resource");
            InputStream inputStream = Resources.getResourceAsStream(resource);

            // 在for循环里每个mapper都重新new一个XMLMapperBuilder，来解析
            XMLMapperBuilder mapperParser = new XMLMapperBuilder(inputStream, configuration, resource);
            mapperParser.parse();
        }
    }

    private void environmentElement(Element environments) throws InstantiationException, IllegalAccessException {
        List<Element> environment = environments.elements("environment");
        String defaultValue = environments.attributeValue("default");
        for (Element e : environment) {
            String id = e.attributeValue("id");
            if (id.equals(defaultValue)){
                TransactionFactory transactionFactory = (TransactionFactory) configuration.getTypeAliasRegistry().resolve(e.element("transactionManager").attributeValue("type")).newInstance();
                Element dataSourceNode = e.element("dataSource");
                DataSourceFactory dataSourceFactory = (DataSourceFactory) configuration.getTypeAliasRegistry().resolve(dataSourceNode.attributeValue("type")).newInstance();
                Properties properties = new Properties();
                List<Element> property = dataSourceNode.elements("property");
                for (Element element : property) {
                    properties.setProperty(element.attributeValue("name"), element.attributeValue("value"));
                }
                dataSourceFactory.setProperties(properties);
                DataSource dataSource = dataSourceFactory.getDataSource();
                Environment.Builder builder = new Environment.Builder(id)
                        .transactionFactory(transactionFactory)
                        .dataSource(dataSource);
                configuration.setEnvironment(builder.build());
            }
        }
    }



}
