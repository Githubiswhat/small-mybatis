package org.example.mybatis.session;

import org.example.mybatis.builder.xml.XmlConfigBuilder;
import org.example.mybatis.session.defaults.DefaultSqlSessionfactory;

import java.io.Reader;

public class SqlSessionFactoryBuilder {

    public SqlSessionFactory build(Reader reader){
        XmlConfigBuilder xmlConfigBuilder = new XmlConfigBuilder(reader);
        return build(xmlConfigBuilder.parse());
    }

    public SqlSessionFactory build(Configuration configuration){
        return new DefaultSqlSessionfactory(configuration);
    }

}
