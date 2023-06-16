package org.example.mybatis.mapping;

import org.example.mybatis.executor.Executor;
import org.example.mybatis.executor.SimpleExecutor;
import org.example.mybatis.executor.parameter.ParameterHandler;
import org.example.mybatis.executor.resultset.DefaultResultSetHandler;
import org.example.mybatis.executor.resultset.ResultSetHandler;
import org.example.mybatis.executor.statement.PreparedStatementHandler;
import org.example.mybatis.executor.statement.StatementHandler;
import org.example.mybatis.reflection.MetaObject;
import org.example.mybatis.scripting.LanguageDriver;
import org.example.mybatis.session.Configuration;
import org.example.mybatis.session.ResultHandler;
import org.example.mybatis.session.RowBounds;
import org.example.mybatis.transaction.Transaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoundSql {

    private String sql;
    private List<ParameterMapping> parameterMappings;
    private Object parameterObject;
    private Map<String, Object> additionalParameters;
    private MetaObject metaParameters;

    public BoundSql(Configuration configuration, String sql, List<ParameterMapping> parameterMappings, Object parameterObject) {
        this.sql = sql;
        this.parameterMappings = parameterMappings;
        this.parameterObject = parameterObject;
        this.additionalParameters = new HashMap<>();
        this.metaParameters = configuration.newMetaObject(additionalParameters);
    }

    public String getSql() {
        return sql;
    }

    public List<ParameterMapping> getParameterMappings() {
        return parameterMappings;
    }

    public Object getParameterObject() {
        return parameterObject;
    }

    public boolean hasAdditionalParameter(String name) {
        return metaParameters.hasGetter(name);
    }

    public void setAdditionalParameter(String name, Object value) {
        metaParameters.setValue(name, value);
    }

    public Object getAdditionalParameter(String name) {
        return metaParameters.getValue(name);
    }


}
