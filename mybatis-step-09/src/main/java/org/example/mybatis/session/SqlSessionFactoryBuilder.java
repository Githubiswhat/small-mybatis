package org.example.mybatis.session;

import org.example.mybatis.builder.xml.XMLConfigBuilder;
import org.example.mybatis.session.Configuration;
import org.example.mybatis.session.SqlSessionFactory;
import org.example.mybatis.session.defaults.DefaultSqlSessionFactory;

import java.io.Reader;

public class SqlSessionFactoryBuilder {

    public SqlSessionFactory build(Reader reader){
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder(reader);
        return build(xmlConfigBuilder.parse());
    }

    private SqlSessionFactory build(Configuration configuration) {
        return new DefaultSqlSessionFactory(configuration);
    }

}
