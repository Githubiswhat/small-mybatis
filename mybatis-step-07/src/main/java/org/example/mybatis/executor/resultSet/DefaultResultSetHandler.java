package org.example.mybatis.executor.resultSet;

import org.example.mybatis.executor.Executor;
import org.example.mybatis.io.Resources;
import org.example.mybatis.mapping.BoundSql;
import org.example.mybatis.mapping.MappedStatement;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DefaultResultSetHandler implements ResultSetHandler{

    private BoundSql boundSql;

    public DefaultResultSetHandler(Executor executor, MappedStatement mappedStatement, BoundSql boundSql) {
        this.boundSql = boundSql;
    }

    @Override
    public <E> List<E> handlerResultSet(Statement statement) throws SQLException {
        ResultSet resultSet = statement.getResultSet();
        try {
            return resultSet2Obj(resultSet, Resources.classForName(boundSql.getReturnType()));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    private <T> List<T> resultSet2Obj(ResultSet resultSet, Class<?> resultType) throws SQLException {
        List<T> list = new ArrayList<>();
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (resultSet.next()) {
                T obj = (T) resultType.newInstance();
                for (int i = 1; i <= columnCount; i++) {
                    Object value = resultSet.getObject(i);
                    String name = metaData.getColumnName(i);
                    Method method = null;
                    String setMethod = "set" + name.substring(0, 1).toUpperCase(Locale.ENGLISH) + name.substring(1);
                    if (value instanceof Timestamp){
                        method = obj.getClass().getMethod(setMethod, Date.class);
                    }else{
                        method = obj.getClass().getMethod(setMethod, value.getClass());
                    }
                    method.invoke(obj, value);
                }
                list.add(obj);
            }
        } catch (SQLException | InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

}
