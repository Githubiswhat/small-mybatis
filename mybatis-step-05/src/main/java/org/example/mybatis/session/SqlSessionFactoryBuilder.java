package org.example.mybatis.session;

import org.example.mybatis.builder.xml.XMLConfigBuilder;
import org.example.mybatis.session.defaults.DefaultSqlSessionfactory;

import java.io.Reader;

public class SqlSessionFactoryBuilder {

    public SqlSessionFactory build(Reader reader){
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder(reader);
        return build(xmlConfigBuilder.parse());
    }

    public SqlSessionFactory build(Configuration configuration){
        return new DefaultSqlSessionfactory(configuration);
    }

}
