package org.example.mybatis.binding;

import org.example.mybatis.mapping.MappedStatement;
import org.example.mybatis.mapping.ParameterMapping;
import org.example.mybatis.mapping.SqlCommandType;
import org.example.mybatis.session.Configuration;
import org.example.mybatis.session.SqlSession;

import java.lang.reflect.Method;
import java.util.*;

public class MapperMethod {

    private SqlCommand command;

    private MethodSignature method;


    public MapperMethod(Class<?> mapperInterface, Method method, Configuration configuration) {
        this.command = new SqlCommand(configuration, mapperInterface, method);
        this.method = new MethodSignature(configuration, method);
    }

    public Object execute(SqlSession sqlSession, Object[] args) {
        Object result = null;
        switch (command.getType()) {
            case INSERT:
                break;
            case DELETE:
                break;
            case UPDATE:
                break;
            case SELECT:
                Object param = method.convertArgsToSqlCommandParam(args);
                result = sqlSession.selectOne(command.getName(), param);
                break;
            default:
                throw new RuntimeException("Unknown execution method for: " + command.getName());
        }
        return result;

    }


    private class SqlCommand {

        private String name;

        private SqlCommandType type;

        public SqlCommand(Configuration configuration, Class<?> mapperInterface, Method method) {
            String id = mapperInterface.getName() + "." + method.getName();
            MappedStatement mappedStatement = configuration.getMappedStatement(id);
            name = mappedStatement.getId();
            type = mappedStatement.getSqlCommandType();
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

    public class MethodSignature {
        private final SortedMap<Integer, String> params;

        public MethodSignature(Configuration configuration, Method method) {
            this.params = Collections.unmodifiableSortedMap(getParams(method));
        }

        private SortedMap<Integer, String> getParams(Method method) {
            TreeMap<Integer, String> params = new TreeMap<>();
            Class<?>[] argTypes = method.getParameterTypes();
            for (int i = 0; i < argTypes.length; i++) {
                String paramName = String.valueOf(argTypes[i]);
                params.put(i, paramName);
            }
            return params;
        }

        public Object convertArgsToSqlCommandParam(Object[] args) {
            final int paramCount = params.size();
            if (args == null || paramCount == 0) {
                return null;
            } else if (paramCount == 1) {
                return args[params.keySet().iterator().next().intValue()];
            } else {
                final Map<String, Object> param = new ParamMap<Object>();
                int i = 0;
                for (Map.Entry<Integer, String> entry : params.entrySet()) {
                    param.put(entry.getValue(), args[entry.getKey().intValue()]);

                    final String genericParamName = "param" + (i - 1);
                    if (!param.containsKey(genericParamName)) {
                        param.put(genericParamName, args[entry.getKey()]);
                    }
                    i++;
                }
                return param;
            }
        }
    }

    public static class ParamMap<V> extends HashMap<String, V> {

        private static final long serialVersionUID = -2212268410512043556L;

        @Override
        public V get(Object key) {
            if (!super.containsKey(key)){
                throw new RuntimeException("Parameter '" + key + "' not found. Available parameters are " + keySet());
            }
            return super.get(key);
        }
    }
}
