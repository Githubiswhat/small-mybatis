package org.example.mybatis.builder.annotation;

import org.example.mybatis.annotations.*;
import org.example.mybatis.binding.MapperMethod;
import org.example.mybatis.builder.MapperBuilderAssistant;
import org.example.mybatis.mapping.SqlCommandType;
import org.example.mybatis.mapping.SqlSource;
import org.example.mybatis.scripting.LanguageDriver;
import org.example.mybatis.session.Configuration;
import org.example.mybatis.session.ResultHandler;
import org.example.mybatis.session.RowBounds;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

public class MapperAnnotationBuilder{

    private final Set<Class<? extends Annotation>> sqlAnnotationTypes = new HashSet<>();

    private Configuration configuration;

    private MapperBuilderAssistant mapperBuilderAssistant;

    private Class<?> type;

    public MapperAnnotationBuilder(Configuration configuration, Class<?> type) {
        String resource = type.getName().replace(".", "/") + ".java (best guess)";
        this.mapperBuilderAssistant = new MapperBuilderAssistant(configuration, resource);
        this.configuration = configuration;
        this.type = type;

        sqlAnnotationTypes.add(Select.class);
        sqlAnnotationTypes.add(Insert.class);
        sqlAnnotationTypes.add(Update.class);
        sqlAnnotationTypes.add(Delete.class);
    }

    public void parse(){
        String resource = type.toString();
        if (!configuration.isResourceLoaded(resource)){
            mapperBuilderAssistant.setCurrentNamespace(type.getName());

            Method[] methods = type.getMethods();
            for (Method method : methods) {
                if (!method.isBridge()){
                    parseStatement(method);
                }
            }
        }
    }

    private void parseStatement(Method method) {
        final Class<?> parameterTypeClass = getParameterType(method);
        LanguageDriver languageDriver = getLanguageDriver(method);
        SqlSource sqlSource = getSqlSourceFromAnnotations(method, parameterTypeClass, languageDriver);

        if (sqlSource != null) {
            String mappedStatementId = type.getName() + "." + method.getName();
            SqlCommandType sqlCommandType = getSqlCommandType(method);
            boolean isSelect = sqlCommandType == SqlCommandType.SELECT;

            String resultMapId = null;
            if (isSelect){
                resultMapId = parseResultMap(method);
            }

            mapperBuilderAssistant.addMappedStatement(
                    mappedStatementId,
                    sqlSource,
                    sqlCommandType,
                    parameterTypeClass,
                    resultMapId,
                    getReturnType(method),
                    languageDriver
            );
        }
    }

    private Class<?> getReturnType(Method method) {
        Class<?> returnType = method.getReturnType();
        if (Collection.class.isAssignableFrom(returnType)){
            Type returnTypeParameter = method.getGenericReturnType();
            if (returnTypeParameter instanceof ParameterizedType) {
                Type[] actualTypeArguments = ((ParameterizedType) returnTypeParameter).getActualTypeArguments();
                if (actualTypeArguments != null && actualTypeArguments.length == 1) {
                    returnTypeParameter = actualTypeArguments[0];
                    if (returnTypeParameter instanceof Class) {
                        returnTypeParameter = ((Class<?>) returnTypeParameter);
                    }else if (returnTypeParameter instanceof ParameterizedType) {
                        returnType = (Class<?>) ((ParameterizedType) returnTypeParameter).getRawType();
                    }else if (returnTypeParameter instanceof GenericArrayType){
                        Class<?> componentType = (Class<?>) ((GenericArrayType) returnTypeParameter).getGenericComponentType();
                        returnType = Array.newInstance(componentType, 0).getClass();
                    }
                }
            }
        }
        return returnType;
    }

    private String parseResultMap(Method method) {
        StringBuilder suffix = new StringBuilder();
        for (Class<?> c : method.getParameterTypes()) {
            suffix.append("-");
            suffix.append(c.getSimpleName());
        }
        if (suffix.length() < 1){
            suffix.append("-void");
        }
        String resultMapId = type.getName() + "." + method.getName() + suffix;
        Class<?> returnType = getReturnType(method);
        mapperBuilderAssistant.addResultMap(resultMapId, returnType, new ArrayList<>());
        return resultMapId;
    }

    private SqlCommandType getSqlCommandType(Method method) {
        Class<? extends Annotation> type = getSqlAnnotationType(method);
        if (type == null) {
            return SqlCommandType.UNKNOWN;
        }
        return SqlCommandType.valueOf(type.getSimpleName().toUpperCase(Locale.ENGLISH));
    }

    private Class<? extends Annotation> getSqlAnnotationType(Method method) {
        for (Class<? extends Annotation> type: sqlAnnotationTypes) {
            Annotation annotation = method.getAnnotation(type);
            if (annotation != null) return type;
        }
        return null;
    }


    private SqlSource getSqlSourceFromAnnotations(Method method, Class<?> parameterType, LanguageDriver languageDriver) {
        try {
            Class<? extends Annotation> sqlAnnotationType = getSqlAnnotationType(method);
            if (sqlAnnotationType != null){
                Annotation sqlAnnotation = method.getAnnotation(sqlAnnotationType);
                final String[] strings = (String[]) sqlAnnotation.getClass().getMethod("value").invoke(sqlAnnotation);
                return buildSqlSourceFromStrings(strings, parameterType, languageDriver);
            }
            return null;
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException("Could not find value method on SQL annotation.  Cause: " + e);
        }
    }

    private SqlSource buildSqlSourceFromStrings(String[] strings, Class<?> parameterTypeClass, LanguageDriver languageDriver) {
        final StringBuilder sql = new StringBuilder();
        for (String fragment : strings) {
            sql.append(fragment);
            sql.append(" ");
        }
        return languageDriver.createSqlSource(configuration, sql.toString(), parameterTypeClass);
    }

    private LanguageDriver getLanguageDriver(Method method) {
        Class<?> langClass = configuration.getLanguageRegistry().getDefaultDriverClass();
        return configuration.getLanguageRegistry().getDriver(langClass);
    }

    private Class<?> getParameterType(Method method) {
        Class<?> parameterType = null;
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (Class<?> clazz : parameterTypes) {
            if (!RowBounds.class.isAssignableFrom(clazz) && !ResultHandler.class.isAssignableFrom(clazz)){
                if (parameterType == null){
                    parameterType = clazz;
                }else {
                    parameterType = MapperMethod.ParamMap.class;
                }
            }
        }
        return parameterType;
    }

}
