package org.example.mybatis.binding;

import org.example.mybatis.mapping.MappedStatement;
import org.example.mybatis.mapping.SqlCommandType;
import org.example.mybatis.session.Configuration;
import org.example.mybatis.session.SqlSession;

import java.lang.reflect.Method;

public class MapperMethod {


    private SqlCommand sqlCommand;

    public MapperMethod(Configuration configuration, Class<?> mapperInterfaces, Method method) {
        sqlCommand = new SqlCommand(configuration, mapperInterfaces, method);
    }

    public Object execute(SqlSession sqlSession, Object args) {
        Object result = null;
        switch (sqlCommand.type){
            case SELECT:
                result = sqlSession.selectOne(sqlCommand.getName(), args);
                break;
            case DELETE:
                break;
            case UPDATE:
                break;
            case INSERT:
                break;
            default:
                throw new RuntimeException("unKnown execution type '" + sqlCommand.type + "'");
        }
        return result;
    }

    private class SqlCommand{
        private String name;

        private SqlCommandType type;

        public SqlCommand(Configuration configuration, Class<?> mapperInterface, Method method) {
            String id = mapperInterface.getName() + "." + method.getName();
            MappedStatement mapperStatement = configuration.getMappedStatement(id);
            name = id;
            type = mapperStatement.getSqlCommandType();
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
