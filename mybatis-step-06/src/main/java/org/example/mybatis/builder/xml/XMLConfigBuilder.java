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
import org.xml.sax.InputSource;

import javax.sql.DataSource;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class XMLConfigBuilder extends BaseBuilder {

    private Element root;

    public XMLConfigBuilder(Reader reader) {
        super(new Configuration());
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(new InputSource(reader));
            root = document.getRootElement();
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }

    public Configuration parse(){
        try {
            mapperElement(root.element("mappers"));
            environmentElement(root.element("environments"));
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return configuration;
    }

    private void environmentElement(Element environments) throws InstantiationException, IllegalAccessException {
        String defaultValue = environments.attributeValue("default");
        List<Element> environmentNodes = environments.elements("environment");
        for (Element e : environmentNodes) {
            String id = e.attributeValue("id");
            if (id.equals(defaultValue)){
                TransactionFactory transactionFactory = (TransactionFactory) configuration.getTypeAliasRegistry().resolveAlias(e.element("transactionManager").attributeValue("type")).newInstance();
                Element dataSourceNode = e.element("dataSource");
                DataSourceFactory dataSourceFactory = (DataSourceFactory) configuration.getTypeAliasRegistry().resolveAlias(dataSourceNode.attributeValue("type")).newInstance();
                Properties properties = new Properties();
                List<Element> property = dataSourceNode.elements("property");
                for (Element element : property) {
                    properties.put(element.attributeValue("name"), element.attributeValue("value"));
                }
                dataSourceFactory.setProperties(properties);
                DataSource dataSource = dataSourceFactory.getDataSource();
                Environment.Builder builder = new Environment.Builder(id)
                        .transactionFactory(transactionFactory)
                        .dataSource(dataSource);
                Environment environment = builder.build();
                configuration.setEnvironment(environment);
            }
        }
    }

    private void mapperElement(Element mappers) {
        List<Element> mapper = mappers.elements("mapper");
        for (Element e : mapper) {
            String resource = e.attributeValue("resource");
            SAXReader saxReader = new SAXReader();
            Reader reader = Resources.getResourceAsReader(resource);
            try {
                Document document = saxReader.read(new InputSource(reader));
                Element root = document.getRootElement();
                String namespace = root.attributeValue("namespace");

                List<Element> selectNodes = root.elements("select");
                for (Element selectNode : selectNodes) {
                    String id = selectNode.attributeValue("id");
                    String parameterType = selectNode.attributeValue("parameterType");
                    String resultType = selectNode.attributeValue("resultType");
                    String sql = selectNode.getText();
                    HashMap<Integer, String> parameter = new HashMap<>();

                    Pattern pattern = Pattern.compile("(#\\{(.*?)\\})");
                    Matcher matcher = pattern.matcher(sql);

                    for (int i = 1; matcher.find(); i++) {
                        String g1 = matcher.group(1);
                        String g2 = matcher.group(2);
                        parameter.put(i, g2);
                        sql = sql.replace(g1, "?");
                    }

                    String mid = namespace + "." + id;
                    String nodeName = selectNode.getName();
                    BoundSql boundSql = new BoundSql(sql, resultType, parameterType, parameter);
                    SqlCommandType sqlCommandType = SqlCommandType.valueOf(nodeName.toUpperCase(Locale.ENGLISH));
                    MappedStatement mappedStatement = new MappedStatement.Builder(configuration, mid, boundSql, sqlCommandType).build();
                    configuration.addMappedStatement(mappedStatement);
                }
                configuration.addMapper(Resources.classForName(namespace));
            } catch (DocumentException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
