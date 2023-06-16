package org.example.mybatis.executor.resultset;


import org.example.mybatis.io.Resources;
import org.example.mybatis.mapping.ResultMap;
import org.example.mybatis.session.Configuration;
import org.example.mybatis.type.JdbcType;
import org.example.mybatis.type.TypeHandler;
import org.example.mybatis.type.TypeHandlerRegistry;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

public class ResultSetWrapper {

    private final ResultSet resultSet;

    private final TypeHandlerRegistry typeHandlerRegistry;

    private final List<String> columnNames = new ArrayList<>();

    private final List<String > classNames = new ArrayList<>();

    private final List<JdbcType> jdbcTypes = new ArrayList<>();

    private final Map<String, Map<Class<?>, TypeHandler<?>>> typeHandlerMap = new HashMap<>();

    private Map<String, List<String>> mappedColumnNamesMap = new HashMap<>();

    private Map<String, List<String>> unMappedColumnNamesMap = new HashMap<>();


    public ResultSetWrapper(ResultSet resultSet, Configuration configuration) throws SQLException {
        super();
        this.resultSet = resultSet;
        this.typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        final ResultSetMetaData metaData = resultSet.getMetaData();
        final int columnCount = metaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            columnNames.add(metaData.getColumnLabel(i));
            jdbcTypes.add(JdbcType.forCode(metaData.getColumnType(i)));
            classNames.add(metaData.getColumnClassName(i));
        }
    }

    public ResultSet getResultSet() {
        return resultSet;
    }

    public TypeHandlerRegistry getTypeHandlerRegistry() {
        return typeHandlerRegistry;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public List<String> getClassNames() {
        return classNames;
    }

    public List<JdbcType> getJdbcTypes() {
        return jdbcTypes;
    }

    public Map<String, Map<Class<?>, TypeHandler<?>>> getTypeHandlerMap() {
        return typeHandlerMap;
    }

    public Map<String, List<String>> getMappedColumnNamesMap() {
        return mappedColumnNamesMap;
    }

    public Map<String, List<String>> getUnMappedColumnNamesMap() {
        return unMappedColumnNamesMap;
    }

    public TypeHandler<?> getTypeHandler(Class<?> propertyType, String columnName) {
        TypeHandler<?> handler = null;
        Map<Class<?>, TypeHandler<?>> columnHandlers = typeHandlerMap.get(columnName);
        if (columnHandlers == null) {
            columnHandlers = new HashMap<>();
            typeHandlerMap.put(columnName, columnHandlers);
        }else {
            handler = columnHandlers.get(propertyType);
        }

        if (handler == null){
            handler = typeHandlerRegistry.getTypeHandler(propertyType, null);
            columnHandlers.put(propertyType, handler);
        }
        return handler;
    }

    private Class<?> resolveClass(String className) {
        try{
            return Resources.classForName(className);
        }catch (ClassNotFoundException e){
            return null ;
        }
    }

    private void loadMappedAndUnMappedColumnNames(ResultMap resultMap, String columnPrefix) throws SQLException{
        List<String> mappedColumnNames = new ArrayList<>();
        List<String> unMappedColumnNames = new ArrayList<>();
        final String unColumnPrefix = columnPrefix == null? null : columnPrefix.toUpperCase(Locale.ENGLISH);
        final Set<String> mappedColumns = prependPrefixes(resultMap.getMappedColumns(), unColumnPrefix);
        for (String columnName : columnNames) {
            final String upperColumnName = columnName.toUpperCase(Locale.ENGLISH);
            if (mappedColumns.contains(upperColumnName)){
                mappedColumnNames.add(upperColumnName);
            }else {
                unMappedColumnNames.add(columnName);
            }
        }
        mappedColumnNamesMap.put(getMapKey(resultMap, columnPrefix), mappedColumnNames);
        unMappedColumnNamesMap.put(getMapKey(resultMap, columnPrefix), unMappedColumnNames);
    }

    private String getMapKey(ResultMap resultMap, String columnPrefix) {
        return resultMap.getId() + ":" + columnPrefix;
    }

    private Set<String> prependPrefixes(Set<String> columnNames, String prefix) {
        if (columnNames == null || columnNames.isEmpty() || prefix == null || prefix.length() == 0) {
            return columnNames;
        }
        final Set<String> prefixed = new HashSet<>();
        for (String columnName : columnNames) {
            prefixed.add(prefix + columnName);
        }
        return prefixed;
    }

    public List<String> getUnMappedColumnNames(ResultMap resultMap, String columnPrefix) throws SQLException {
        List<String> unMappedColumnNames = unMappedColumnNamesMap.get(getMapKey(resultMap, columnPrefix));
        if (unMappedColumnNames == null) {
            loadMappedAndUnMappedColumnNames(resultMap, columnPrefix);
            unMappedColumnNames = unMappedColumnNamesMap.get(getMapKey(resultMap, columnPrefix));
        }
        return unMappedColumnNames;
    }

}
