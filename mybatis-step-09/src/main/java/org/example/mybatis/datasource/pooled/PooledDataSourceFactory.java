package org.example.mybatis.datasource.pooled;

import org.example.mybatis.datasource.unpooled.UnPooledDataSource;
import org.example.mybatis.datasource.unpooled.UnPooledDataSourceFactory;

import javax.sql.DataSource;

public class PooledDataSourceFactory extends UnPooledDataSourceFactory{

    @Override
    public DataSource getDataSource() {
        PooledDataSource dataSource = new PooledDataSource();
        dataSource.setDriverClassName(properties.getProperty("driver"));
        dataSource.setUrl(properties.getProperty("url"));
        dataSource.setUsername(properties.getProperty("username"));
        dataSource.setPassword(properties.getProperty("password"));
        return dataSource;
    }
}
