package org.example.mybatis.reflection.wrapper;

import org.example.mybatis.reflection.MetaObject;
import org.example.mybatis.reflection.factory.ObjectFactory;
import org.example.mybatis.reflection.property.PropertyTokenizer;

import java.util.List;

public interface ObjectWrapper {

    Object get(PropertyTokenizer prop);

    void set(PropertyTokenizer prop, Object value);

    String findProperty(String name, boolean useCamelCaseMapping);

    String[] getGetterNames();

    String[] getSetterNames();

    Class<?> getSetterType(String name);

    Class<?> getGetterType(String name);

    boolean hasGetter(String name);

    boolean hasSetter(String name);

    MetaObject instantiatePropertyValue(String name, PropertyTokenizer propertyTokenizer, ObjectFactory objectFactory);

    boolean isCollection();

    void add(Object element);

    <E> void addAll(List<E> element);

}
