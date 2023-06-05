package org.example.mybatis.binding;

import org.example.mybatis.mapping.MappedStatement;
import org.example.mybatis.mapping.SqlCommandType;
import org.example.mybatis.session.Configuration;
import org.example.mybatis.session.SqlSession;

import java.lang.reflect.Method;

public class MapperMethod {

    private SqlCommand sqlCommand;

    public MapperMethod(Configuration configuration, Method method, Class<?> mapperInterface) {
        sqlCommand = new SqlCommand(configuration, method, mapperInterface);
    }


    public Object execute(SqlSession sqlSession, Object[] args) {
        Object result = null;
        switch (sqlCommand.getType()){
            case DELETE:
                break;
            case INSERT:
                break;
            case UPDATE:
                break;
            case SELECT:
                result = sqlSession.selectOne(sqlCommand.getName(), args);
                break;
            default:
                throw new RuntimeException("unknown execute sql command " + sqlCommand.type);
        }
        return result;
    }

    private static class SqlCommand{

        private String name;

        private SqlCommandType type;

        public SqlCommand(Configuration configuration, Method method, Class<?> mapperInterface) {
            String statement = mapperInterface.getName() + "." + method.getName();
            MappedStatement mappedStatement = configuration.getMappedStatement(statement);
            type = mappedStatement.getSqlCommandType();
            name = mappedStatement.getId();
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public SqlCommandType getType() {
            return type;
        }

        public void setType(SqlCommandType type) {
            this.type = type;
        }
    }

}
