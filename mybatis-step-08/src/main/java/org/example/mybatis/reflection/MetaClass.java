package org.example.mybatis.reflection;

import org.example.mybatis.reflection.invoker.GetFieldInvoker;
import org.example.mybatis.reflection.invoker.Invoker;
import org.example.mybatis.reflection.invoker.MethodInvoker;
import org.example.mybatis.reflection.property.PropertyTokenizer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;

public class MetaClass {


    private Reflector reflector;

    private MetaClass(Class<?> type){
        this.reflector = Reflector.forClass(type);
    }

    public static MetaClass forClass(Class<?> type){
        return new MetaClass(type);
    }

    public static boolean isClassCacheEnabled(){
        return Reflector.isClassCacheEnabled();
    }

    public static void setClassCacheEnabled(boolean classCacheEnabled){
        Reflector.setClassCacheEnabled(classCacheEnabled);
    }

    public MetaClass metaClassForProperty(String name){
        Class<?> propType = reflector.getGetterType(name);
        return MetaClass.forClass(propType);
    }

    public String findProperty(String name) {
        StringBuilder prop = buildProperty(name, new StringBuilder());
        return prop.length() > 0 ? prop.toString() : null;
    }

    private StringBuilder buildProperty(String name, StringBuilder stringBuilder) {
        PropertyTokenizer propertyTokenizer = new PropertyTokenizer(name);
        if (propertyTokenizer.hasNext()){
            String propertyName = reflector.findPropertyName(propertyTokenizer.getName());
            if (propertyName != null){
                stringBuilder.append(propertyName);
                stringBuilder.append(".");
                MetaClass metaProp = metaClassForProperty(propertyName);
                metaProp.buildProperty(propertyTokenizer.getChildren(), stringBuilder);
            }
        }else {
            String propertyName = reflector.findPropertyName(name);
            if (propertyName != null){
                stringBuilder.append(propertyName);
            }
        }
        return stringBuilder;
    }

    public String findProperty(String name, boolean useCamelCaseMapping) {
        if (useCamelCaseMapping){
            name = name.replace("_", "");
        }
        return findProperty(name);
    }

    public String[] getGetterNames() {
        return reflector.getGetablePropertyNames();
    }

    public String[] getSetterNames() {
        return reflector.getSetablePropertyNames();
    }

    public Class<?> getSetterType(String name) {
        PropertyTokenizer propertyTokenizer = new PropertyTokenizer(name);
        if (propertyTokenizer.hasNext()){
            MetaClass metaProp = metaClassForProperty(propertyTokenizer.getName());
            return metaProp.getSetterType(propertyTokenizer.getChildren());
        }else {
            return reflector.getSetterType(propertyTokenizer.getName());
        }
    }

    public Class<?> getGetterType(String name) {
        PropertyTokenizer propertyTokenizer = new PropertyTokenizer(name);
        if (propertyTokenizer.hasNext()){
            MetaClass metaProp = metaClassForProperty(propertyTokenizer.getName());
            return metaProp.getGetterType(propertyTokenizer.getChildren());
        }else {
            return reflector.getGetterType(propertyTokenizer.getName());
        }
    }

    private Class<?> getGetterType(PropertyTokenizer propertyTokenizer){
        Class<?> type = reflector.getGetterType(propertyTokenizer.getName());
        if (propertyTokenizer.getIndex() != null && Collections.class.isAssignableFrom(type)){
            Type returnType = getGenericGetterType(propertyTokenizer.getName());
            if (returnType instanceof ParameterizedType){
                Type[] actualTypeArguments = ((ParameterizedType) returnType).getActualTypeArguments();
                if (actualTypeArguments != null && actualTypeArguments.length == 1){
                    returnType = actualTypeArguments[0];
                    if (returnType instanceof Class){
                        type = ((Class<?>) returnType);
                    }else if (returnType instanceof ParameterizedType){
                        type = ((Class<?>) ((ParameterizedType) returnType).getRawType());
                    }
                }
            }
        }
        return type;
    }

    private Type getGenericGetterType(String name) {
        try {
            Invoker invoker = reflector.getGetInvoker(name);
            if (invoker instanceof MethodInvoker){
                Field _method = MethodInvoker.class.getDeclaredField("method");
                _method.setAccessible(true);
                Method method = (Method) _method.get(invoker);
                return method.getGenericReturnType();
            }else if (invoker instanceof GetFieldInvoker){
                Field _field = GetFieldInvoker.class.getDeclaredField("field");
                _field.setAccessible(true);
                Field field = (Field) _field.get(invoker);
                return field.getGenericType();
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {

        }
        return null;
    }

    public Invoker getGetInvoker(String name) {
        return reflector.getGetInvoker(name);
    }

    public Invoker getSetterInvoker(String name) {
        return reflector.getSetInvoker(name);
    }


    public boolean hasGetter(String name) {
        PropertyTokenizer propertyTokenizer = new PropertyTokenizer(name);
        if (propertyTokenizer.hasNext()){
            if (reflector.hasGetter(propertyTokenizer.getName())){
                MetaClass metaProp = metaClassForProperty(propertyTokenizer.getName());
                return metaProp.hasGetter(propertyTokenizer.getChildren());
            }else {
                return false;
            }
        }else {
            return reflector.hasGetter(propertyTokenizer.getName());
        }
    }

    public boolean hasSetter(String name) {
        PropertyTokenizer propertyTokenizer = new PropertyTokenizer(name);
        if (propertyTokenizer.hasNext()){
            if (reflector.hasSetter(propertyTokenizer.getName())){
                MetaClass metaProp = metaClassForProperty(propertyTokenizer.getName());
                return metaProp.hasSetter(propertyTokenizer.getChildren());
            }else {
                return false;
            }
        }else {
            return reflector.hasSetter(propertyTokenizer.getName());
        }
    }

    public boolean hasDefaultConstructor(){
        return reflector.hasDefaultConstructor();
    }
}
