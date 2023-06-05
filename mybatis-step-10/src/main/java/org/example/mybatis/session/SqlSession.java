package org.example.mybatis.session;

public interface SqlSession {

    <T> T selectOne(String statement);

    <T> T selectOne(String statement, Object args);

    <T> T getMapper(Class<T> type);

    Configuration getConfiguration();

}
