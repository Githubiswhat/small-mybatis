package org.example.mybatis;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.Date;
import java.util.*;


public class DefaultSqlSession implements SqlSession{

    private Connection connection;

    private Map<String, XNode> mapperElement;

    public DefaultSqlSession(Connection connection, Map<String, XNode> mapperElement) {
        this.connection = connection;
        this.mapperElement = mapperElement;
    }

    @Override
    public <T> T selectOne(String statement) {
        try {
            XNode xNode = mapperElement.get(statement);
            PreparedStatement preparedStatement = connection.prepareStatement(xNode.getSql());
            ResultSet resultSet = preparedStatement.executeQuery();
            List<T> objects = resultSet2Objects(resultSet, Class.forName(xNode.getResultType()));
            return objects.get(0);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        XNode xNode = mapperElement.get(statement);
        Map<Integer, String> parameters = xNode.getParameters();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(xNode.getSql());
            buildParameters(preparedStatement, parameter, parameters);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<T> objects = resultSet2Objects(resultSet, Class.forName(xNode.getResultType()));
            return objects.get(0);
        } catch (SQLException | ClassNotFoundException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void buildParameters(PreparedStatement preparedStatement, Object parameter, Map<Integer, String> parameters) throws SQLException, IllegalAccessException {
        int size = parameters.size();

        if (parameter instanceof Long){
            for (int i = 1; i <= size; i++) {
                preparedStatement.setLong(i, Long.parseLong(parameter.toString()));
            }
            return;
        }

        if (parameter instanceof Integer){
            for (int i = 1; i <= size; i++) {
                preparedStatement.setInt(i, Integer.parseInt(parameter.toString()));
            }
            return;
        }

        if (parameter instanceof String){
            for (int i = 1; i <= size; i++) {
                preparedStatement.setString(i, parameter.toString());
            }
            return;
        }

        HashMap<String, Object> filedMap = new HashMap<>();
        Field[] declaredFields = parameter.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            String name = declaredField.getName();
            declaredField.setAccessible(true);
            Object value = declaredField.get(parameter);
            declaredField.setAccessible(false);
            filedMap.put(name, value);
        }

        for (int i = 1; i <= size; i++) {
            String paramDefine = parameters.get(i);
            Object obj = filedMap.get(paramDefine);

            if (obj instanceof Long){
                preparedStatement.setLong(i, Long.parseLong(obj.toString()));
            }

            if (obj instanceof Short){
                preparedStatement.setShort(i, Short.parseShort(obj.toString()));
            }

            if (obj instanceof Integer){
                preparedStatement.setInt(i, Integer.parseInt(obj.toString()));
            }

            if (obj instanceof String){
                preparedStatement.setString(i, obj.toString());
            }

            if (obj instanceof Date){
                preparedStatement.setDate(i, (java.sql.Date) obj);
            }
        }
    }

    @Override
    public <T> List<T> selectList(String statement) {
        try {
            XNode xNode = mapperElement.get(statement);
            PreparedStatement preparedStatement = connection.prepareStatement(xNode.getSql());
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet2Objects(resultSet, Class.forName(xNode.getResultType()));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> List<T> selectList(String statement, Object parameter) {
        XNode xNode = mapperElement.get(statement);
        Map<Integer, String> parameters = xNode.getParameters();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(xNode.getSql());
            buildParameters(preparedStatement, parameter, parameters);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet2Objects(resultSet, Class.forName(xNode.getResultType()));
        } catch (SQLException | ClassNotFoundException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void close() {
        if (connection == null){
            return;
        }
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private <T> List<T> resultSet2Objects(ResultSet resultSet, Class<?> clazz) {
        List<T> list = new ArrayList<>();

        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (resultSet.next()){
                T obj = (T) clazz.newInstance();
                for (int i = 1; i <= columnCount; i++) {
                    Object value = resultSet.getObject(i);
                    String columnName = metaData.getColumnName(i);
                    String setMethod = "set" + columnName.substring(0, 1).toUpperCase() + columnName.substring(1);
                    Method method;
                    if (value instanceof Timestamp){
                        method = clazz.getMethod(setMethod, Date.class);
                    }else{
                        method = clazz.getMethod(setMethod, value.getClass());
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
