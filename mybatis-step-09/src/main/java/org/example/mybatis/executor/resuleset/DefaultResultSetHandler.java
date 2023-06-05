package org.example.mybatis.executor.resuleset;

import org.example.mybatis.mapping.BoundSql;
import org.example.mybatis.mapping.MappedStatement;

import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class DefaultResultSetHandler implements ResultSetHandler{

    private BoundSql boundSql;

    private MappedStatement mappedStatement;

    public DefaultResultSetHandler(BoundSql boundSql, MappedStatement mappedStatement) {
        this.boundSql = boundSql;
        this.mappedStatement = mappedStatement;
    }

    @Override
    public <E> List<E> handlerResultSet(Statement statement) throws SQLException{
        ResultSet resultSet = statement.getResultSet();
        return resultSet2Obj(resultSet, mappedStatement.getResultType());
    }

    private <E> List<E> resultSet2Obj(ResultSet resultSet, Class<?> clazz) {
        List<E> list = new ArrayList<>();
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            // 每次遍历行值
            while (resultSet.next()) {
                E obj = (E) clazz.newInstance();
                for (int i = 1; i <= columnCount; i++) {
                    Object value = resultSet.getObject(i);
                    String columnName = metaData.getColumnName(i);
                    String setMethod = "set" + columnName.substring(0, 1).toUpperCase() + columnName.substring(1);
                    Method method;
                    if (value instanceof Timestamp) {
                        method = clazz.getMethod(setMethod, Date.class);
                    } else {
                        method = clazz.getMethod(setMethod, value.getClass());
                    }
                    method.invoke(obj, value);
                }
                list.add(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
