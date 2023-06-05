package org.example.mybatis.datasource.unpooled;

import com.alibaba.druid.pool.DruidDataSource;
import org.example.mybatis.datasource.DataSourceFactory;

import javax.sql.DataSource;
import java.util.Properties;

public class UnPooledDataSourceFactory implements DataSourceFactory {

    protected Properties properties;

    public UnPooledDataSourceFactory() {
    }

    public UnPooledDataSourceFactory(Properties properties) {
        this.properties = properties;
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    public DataSource getDataSource() {
        UnPooledDataSource dataSource = new UnPooledDataSource();
        dataSource.setDriverClassName(properties.getProperty("driver"));
        dataSource.setUrl(properties.getProperty("url"));
        dataSource.setUsername(properties.getProperty("username"));
        dataSource.setPassword(properties.getProperty("password"));
        return dataSource;
    }
}
