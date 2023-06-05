package org.example.mybatis.binding;

import org.example.mybatis.mapping.MappedStatement;
import org.example.mybatis.mapping.SqlCommandType;
import org.example.mybatis.session.Configuration;
import org.example.mybatis.session.SqlSession;

import java.lang.reflect.Method;
import java.sql.SQLException;

public class MapperMethod {

    private SqlCommand command;
    public Object execute(SqlSession sqlSession, Object[] args){
        Object result = null;
        switch (command.type){
            case UPDATE:
                break;
            case INSERT:
                break;
            case SELECT:
                result = sqlSession.selectOne(command.getName(), args);
                break;
            case DELETE:
                break;
            default:
                throw new RuntimeException("unknown execute command type: " + command.type);
        }
        return result;
    }

    public MapperMethod(Configuration configuration, Class<?> mapperInterface, Method method) {
        this.command = new SqlCommand(configuration, mapperInterface, method);
    }

    private class SqlCommand {

        private String name;

        private SqlCommandType type;

        public SqlCommand(Configuration configuration, Class<?> mapperInterface, Method method) {
            String id = mapperInterface.getName() + "." + method.getName();
            MappedStatement mappedStatement = configuration.getMappedStatement(id);
            this.name = id;
            this.type = mappedStatement.getSqlCommandType();
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
