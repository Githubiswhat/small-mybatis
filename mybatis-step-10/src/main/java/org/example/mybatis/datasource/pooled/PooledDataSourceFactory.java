package org.example.mybatis.datasource.pooled;

import org.example.mybatis.datasource.unpooled.UnPooledDataSource;
import org.example.mybatis.datasource.unpooled.UnPooledDataSourceFactory;

import javax.sql.DataSource;

public class PooledDataSourceFactory extends UnPooledDataSourceFactory {

    public PooledDataSourceFactory() {
        this.dataSource = new PooledDataSource();
    }


}
