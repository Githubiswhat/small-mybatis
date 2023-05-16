package org.example.mybatis.binding;

import org.example.mybatis.Mapping.MappedStatement;
import org.example.mybatis.Mapping.SqlCommandType;
import org.example.mybatis.session.Configuration;
import org.example.mybatis.session.SqlSession;

import java.lang.reflect.Method;

public class MapperMethod {

    private SqlCommand command;

    public <T> MapperMethod(Configuration configuration, Method method, Class<T> methodInterface) {
        command = new SqlCommand(configuration, method, methodInterface);
    }

    public Object execute(SqlSession session, Object[] objects) {
        Object result = null;
        switch (command.type){
            case DELETE:
                break;
            case INSERT:
                break;
            case UPDATE:
                break;
            case SELECT:
                result = session.selectOne(command.name, objects);
                break;
            default:
                throw new RuntimeException("the execution method " + command.type + "don't know");
        }
        return result;
    }

    private static class SqlCommand{
        private String name;

        private SqlCommandType type;

        public SqlCommand(Configuration configuration, Method method, Class<?> methodInterface) {
            String statement = methodInterface.getName() + "." + method.getName();
            MappedStatement mappedStatement = configuration.getMappedStatement(statement);
            name = mappedStatement.getId();
            type = mappedStatement.getSqlCommandType();
        }

        public String getName() {
            return name;
        }

        public SqlCommandType getType() {
            return type;
        }
    }
}
