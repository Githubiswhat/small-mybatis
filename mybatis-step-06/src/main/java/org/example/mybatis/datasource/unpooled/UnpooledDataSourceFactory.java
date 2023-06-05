package org.example.mybatis.datasource.unpooled;

import org.example.mybatis.datasource.DataSourceFactory;

import javax.sql.DataSource;
import java.util.Properties;

public class UnpooledDataSourceFactory implements DataSourceFactory {

    protected Properties properties;

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    public DataSource getDataSource() {
        UnPooledDataSource unPooledDataSource = new UnPooledDataSource();
        unPooledDataSource.setDriverClassName(properties.getProperty("driver"));
        unPooledDataSource.setUrl(properties.getProperty("url"));
        unPooledDataSource.setUsername(properties.getProperty("username"));
        unPooledDataSource.setPassword(properties.getProperty("password"));
        return unPooledDataSource;
    }
}
