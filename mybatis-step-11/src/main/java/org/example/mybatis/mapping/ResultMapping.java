package org.example.mybatis.mapping;

import org.example.mybatis.session.Configuration;
import org.example.mybatis.type.JdbcType;
import org.example.mybatis.type.TypeHandler;

public class ResultMapping {

    private Configuration configuration;

    private String property;

    private String column;

    private Class<?> javaType;

    private JdbcType jdbcType;

    private TypeHandler<?>  typeHandler;

    public ResultMapping() {
    }

    public static class Builder{
        private ResultMapping resultMapping = new ResultMapping();



    }







}
