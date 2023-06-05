package org.example.mybatis.session.defaults;

import org.example.mybatis.Mapping.BoundSql;
import org.example.mybatis.Mapping.Environment;
import org.example.mybatis.Mapping.MappedStatement;
import org.example.mybatis.session.Configuration;
import org.example.mybatis.session.SqlSession;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        try {
            MappedStatement mappedStatement = configuration.getMappedStatement(statement);
            Environment environment = configuration.getEnvironment();

            BoundSql boundSql = mappedStatement.getBoundSql();

            Connection connection = environment.getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSql());
            preparedStatement.setLong(1, Long.parseLong(((Object[]) parameter)[0].toString()));
            ResultSet resultSet = preparedStatement.executeQuery();
            List<T> objList = resultSet2Obj(resultSet, Class.forName(boundSql.getResultType()));
            return objList.get(0);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> List<T> resultSet2Obj(ResultSet resultSet, Class<?> clazz) {
        List<T> resultList = new ArrayList<>();
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (resultSet.next()) {
                T obj = (T) clazz.newInstance();
                for (int i = 1; i <= columnCount; i++) {
                    Object value = resultSet.getObject(i);
                    String columnName = metaData.getColumnName(i);
                    String setMethod = "set" + columnName.substring(0, 1).toUpperCase() + columnName.substring(1);
                    Method method;
                    if (value instanceof Timestamp){
                        method = clazz.getDeclaredMethod(setMethod, Date.class);
                    }else {
                        method = clazz.getDeclaredMethod(setMethod, value.getClass());
                    }
                    method.invoke(obj, value);
                }
                resultList.add(obj);
            }
        } catch (SQLException | InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return resultList;
    }


    @Override
    public <T> T getMapper(Class<?> type) {
        return configuration.getMapper(type, this);
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

}
