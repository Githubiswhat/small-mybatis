package org.example.mybatis.mapping;

import org.example.mybatis.transaction.TransactionFactory;

import javax.sql.DataSource;

public class Environment {

    private String id;

    private DataSource dataSource;

    private TransactionFactory transactionFactory;

    public Environment(String id, DataSource dataSource, TransactionFactory transactionFactory) {
        this.id = id;
        this.dataSource = dataSource;
        this.transactionFactory = transactionFactory;
    }

    public static class Builder{
        private String id;

        private DataSource dataSource;

        private TransactionFactory transactionFactory;

        public Builder(String id) {
            this.id = id;
        }

        public Builder dataSource(DataSource dataSource){
            this.dataSource = dataSource;
            return this;
        }

        public Builder transactionFactory(TransactionFactory transactionFactory){
            this.transactionFactory = transactionFactory;
            return this;
        }

        public Environment build() {
            return new Environment(id, dataSource, transactionFactory);
        }
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public TransactionFactory getTransactionFactory() {
        return transactionFactory;
    }

    public void setTransactionFactory(TransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
    }
}
