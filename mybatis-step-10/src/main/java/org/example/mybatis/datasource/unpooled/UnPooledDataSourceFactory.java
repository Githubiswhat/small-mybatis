package org.example.mybatis.datasource.unpooled;

import org.example.mybatis.datasource.DataSourceFactory;
import org.example.mybatis.reflection.MetaObject;
import org.example.mybatis.reflection.SystemMetaObject;

import javax.sql.DataSource;
import java.util.Properties;

public class UnPooledDataSourceFactory implements DataSourceFactory {

    protected DataSource dataSource;

    public UnPooledDataSourceFactory() {
        this.dataSource = new UnPooledDataSource();
    }

    @Override
    public void setProperties(Properties properties) {
        MetaObject metaObject = SystemMetaObject.forObject(dataSource);
        for (Object key : properties.keySet()) {
            String propertyName = (String) key;
            if (metaObject.hasSetter(propertyName)){
                String value = (String) properties.get(propertyName);
                Object convertedValue = convertValue(metaObject, propertyName, value);
                metaObject.setValue(propertyName, convertedValue);
            }
        }
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    private Object convertValue(MetaObject metaObject, String propertyName, String value) {
        Object convertedValue = value;
        Class<?> targetType =  metaObject.getSetterType(propertyName);
        if (targetType == Integer.class || targetType == int.class){
            convertedValue = Integer.valueOf(value);
        }else if (targetType == Long.class || targetType == long.class){
            convertedValue = Long.valueOf(value);
        }else if (targetType == Boolean.class || targetType == boolean.class){
            convertedValue = Boolean.valueOf(value);
        }
        return convertedValue;
    }

}
