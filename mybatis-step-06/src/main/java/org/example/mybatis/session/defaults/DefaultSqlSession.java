package org.example.mybatis.session.defaults;

import org.example.mybatis.io.Resources;
import org.example.mybatis.mapping.BoundSql;
import org.example.mybatis.session.Configuration;
import org.example.mybatis.session.SqlSession;

import java.lang.reflect.Method;
import java.sql.*;
import java.util.Date;

public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <T> T selectOne(String statement) {
        return (T) ("你的调用被代理了" + statement);
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        try (Connection connection = configuration.getEnvironment().getDataSource().getConnection()){
            BoundSql boundSql = configuration.getMappedStatement(statement).getBoundSql();
            PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSql());
            preparedStatement.setLong(1, Long.parseLong(((Object[]) parameter)[0].toString()));
            ResultSet resultSet = preparedStatement.executeQuery();
            T  result = (T) resultSet2Object(resultSet, Resources.classForName(boundSql.getResultType()));
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object resultSet2Object(ResultSet resultSet, Class<?> clazz) throws Exception {
        Object result = null;
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        while (resultSet.next()) {
            Object obj = clazz.newInstance();
            for (int i = 1; i <= columnCount; i++) {
                Object value = resultSet.getObject(i);
                String name = metaData.getColumnName(i);
                Method method;
                String setMethod = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
                if (value instanceof Timestamp){
                    method = clazz.getMethod(setMethod, Date.class);
                }else {
                    method = clazz.getMethod(setMethod, value.getClass());
                }
                method.invoke(obj, value);
            }
            result = obj;
        }
        return result;
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        return configuration.getMapper(type, this);
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }
}
