package org.example.mybatis.binding;

import org.dom4j.Branch;
import org.example.mybatis.mapping.MappedStatement;
import org.example.mybatis.mapping.SqlCommandType;
import org.example.mybatis.session.Configuration;
import org.example.mybatis.session.SqlSession;

import java.lang.reflect.Method;

public class MapperMethod {

    private final SqlCommand sqlCommand;

    public MapperMethod(Configuration configuration, Class<?> mapperInterface, Method method) {
        this.sqlCommand = new SqlCommand(configuration, mapperInterface, method);
    }

    public Object execute(SqlSession sqlSession, Object[] args){
        Object result = null;
        switch (sqlCommand.getType()){
            case DELETE:
                break;
            case INSERT:
                break;
            case SELECT:
                result = sqlSession.selectOne(sqlCommand.getName(), args);
                break;
            case UPDATE:
                break;
            default:
                throw new RuntimeException("unknown execution method for: " + sqlCommand.getName());
        }
        return result;
    }


    public static class SqlCommand{

        private String name;

        private SqlCommandType type;

        public SqlCommand(Configuration configuration, Class<?> mapperInterface, Method method) {
            String statement = mapperInterface.getName() + "." + method.getName();
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
