package org.example.mybatis.reflection.wrapper;

import org.example.mybatis.reflection.MetaObject;
import org.example.mybatis.reflection.factory.ObjectFactory;
import org.example.mybatis.reflection.property.PropertyTokenizer;

import java.util.Collection;
import java.util.List;

public class CollectionWrapper implements ObjectWrapper{

    private Collection<Object> object;

    public CollectionWrapper(MetaObject metaObject, Collection object) {
        this.object = object;
    }

    @Override
    public Object get(PropertyTokenizer propertyTokenizer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void set(PropertyTokenizer propertyTokenizer, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String findProperty(String name, boolean useCamelCaseMapping) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String[] getGetterNames() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String[] getSetterNames() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<?> getSetterType(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<?> getGetterType(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasGetter(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasSetter(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MetaObject instantiatePropertyValue(String name, PropertyTokenizer propertyTokenizer, ObjectFactory objectFactory) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isCollection() {
        return true;
    }

    @Override
    public void add(Object element) {
        object.add(element);
    }

    @Override
    public <E> void addAll(List<E> elements) {
        object.add(elements);
    }
}
