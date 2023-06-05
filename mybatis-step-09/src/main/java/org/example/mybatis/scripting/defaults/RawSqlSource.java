package org.example.mybatis.scripting.defaults;

import org.example.mybatis.builder.SqlSourceBuilder;
import org.example.mybatis.mapping.BoundSql;
import org.example.mybatis.mapping.SqlSource;
import org.example.mybatis.scripting.xmltags.DynamicContext;
import org.example.mybatis.scripting.xmltags.SqlNode;
import org.example.mybatis.session.Configuration;

import java.util.HashMap;

public class RawSqlSource implements SqlSource {

    private final SqlSource sqlSource;

    public RawSqlSource(Configuration configuration, SqlNode rootSqlNode, Class<?> parameterType) {
        this(configuration, getSql(configuration, rootSqlNode), parameterType);
    }

    public RawSqlSource(Configuration configuration, String sql, Class<?> parameterType) {
        SqlSourceBuilder sqlSourceBuilder = new SqlSourceBuilder(configuration);
        Class<?> clazz = parameterType == null ? Object.class : parameterType;
        sqlSource = sqlSourceBuilder.parse(sql, clazz, new HashMap<>());
    }

    private static String getSql(Configuration configuration, SqlNode rootSqlNode) {
        DynamicContext dynamicContext = new DynamicContext(configuration, null);
        rootSqlNode.apply(dynamicContext);
        return dynamicContext.getSql();
    }

    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        return sqlSource.getBoundSql(parameterObject);
    }
}
