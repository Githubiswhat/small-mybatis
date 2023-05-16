package org.example.mybatis.session;

import org.example.mybatis.builder.xml.XmlConfigBuilder;
import org.example.mybatis.session.defaults.DefaultSqlSessionFactory;

import java.io.Reader;

public class SqlSessionFactoryBuilder {

    public SqlSessionFactory build(Reader reader){
        XmlConfigBuilder xmlBuilder = new XmlConfigBuilder(reader);
        return build(xmlBuilder.parse());
    }

    private SqlSessionFactory build(Configuration configuration) {
        return new DefaultSqlSessionFactory(configuration);
    }

}
