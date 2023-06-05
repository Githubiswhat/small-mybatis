package org.example.mybatis.datasource.druid;

import com.alibaba.druid.pool.DruidDataSource;
import org.example.mybatis.datasource.DataSourceFactory;

import javax.sql.DataSource;
import java.util.Properties;

public class DruidDataSourceFactory implements DataSourceFactory {

    private Properties properties;


    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    public DataSource getDataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(properties.getProperty("driver"));
        druidDataSource.setUrl(properties.getProperty("url"));
        druidDataSource.setUsername(properties.getProperty("username"));
        druidDataSource.setPassword(properties.getProperty("password"));
        return druidDataSource;
    }
}
