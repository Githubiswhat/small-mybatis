package org.example.mybatis.reflection;

import org.example.mybatis.reflection.factory.ObjectFactory;
import org.example.mybatis.reflection.property.PropertyTokenizer;
import org.example.mybatis.reflection.wrapper.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MetaObject {

    private Object originalObject;

    private ObjectWrapper objectWrapper;

    private ObjectFactory objectFactory;

    private ObjectWrapperFactory objectWrapperFactory;

    public MetaObject(Object object, ObjectFactory objectFactory, ObjectWrapperFactory objectWrapperFactory) {
        this.originalObject = object;
        this.objectFactory = objectFactory;
        this.objectWrapperFactory = objectWrapperFactory;

        if (object instanceof ObjectWrapper) {
            this.objectWrapper = (ObjectWrapper) object;
        }else if (objectWrapperFactory.hasWrapper(object)){
            this.objectWrapper = objectWrapperFactory.getWrapperFor(this, object);
        }else if (object instanceof Map){
            this.objectWrapper = new MapWrapper(this, (Map) object);
        }else if (object instanceof Collection){
            this.objectWrapper = new CollectionWrapper(this, (Collection) object);
        }else {
            this.objectWrapper = new BeanWrapper(this, object);
        }
    }

    public static MetaObject forObject(Object object, ObjectFactory objectFactory, ObjectWrapperFactory objectWrapperFactory){
        if (object == null){
            return SystemMetaObject.NULL_META_OBJECT;
        }else {
            return new MetaObject(object, objectFactory, objectWrapperFactory);
        }
    }

    public Object getOriginalObject() {
        return originalObject;
    }

    public ObjectFactory getObjectFactory() {
        return objectFactory;
    }

    public ObjectWrapperFactory getObjectWrapperFactory() {
        return objectWrapperFactory;
    }


    public String findProperty(String propName, boolean useCamelCaseMapping){
        return objectWrapper.findProperty(propName, useCamelCaseMapping);
    }

    public String[] getGetterNames() {
        return objectWrapper.getGetterNames();
    }


    public String[] getSetterNames() {
        return objectWrapper.getSetterNames();
    }

    public Class<?> getSetterType(String name){
        return objectWrapper.getSetterType(name);
    }

    public Class<?> getGetterType(String name){
        return objectWrapper.getGetterType(name);
    }

    public boolean hasGetter(String name){
        return objectWrapper.hasGetter(name);
    }

    public boolean hasSetter(String name){
        return objectWrapper.hasSetter(name);
    }

    public Object getValue(String name){
        PropertyTokenizer propertyTokenizer = new PropertyTokenizer(name);
        if (propertyTokenizer.hasNext()){
            MetaObject metaValue = metaObjectForProperty(propertyTokenizer.getIndexedName());
            if (metaValue == SystemMetaObject.NULL_META_OBJECT){
                return null;
            }else {
                return metaValue.getValue(propertyTokenizer.getChildren());
            }
        }else {
            return objectWrapper.get(propertyTokenizer);
        }
    }

    public MetaObject metaObjectForProperty(String name) {
        Object value = getValue(name);
        return MetaObject.forObject(value, objectFactory, objectWrapperFactory);
    }


    public void setValue(String name, Object value){
        PropertyTokenizer propertyTokenizer = new PropertyTokenizer(name);
        if (propertyTokenizer.hasNext()){
            MetaObject metaValue = metaObjectForProperty(propertyTokenizer.getIndexedName());
            if (metaValue == SystemMetaObject.NULL_META_OBJECT){
                if (value == null && propertyTokenizer.getChildren() != null){
                    return;
                }else {
                    metaValue = objectWrapper.instantiatePropertyValue(name, propertyTokenizer, objectFactory);
                }
            }
            metaValue.setValue(propertyTokenizer.getChildren(), value);
        }else {
            objectWrapper.set(propertyTokenizer, value);
        }
    }

    public ObjectWrapper getObjectWrapper() {
        return objectWrapper;
    }

    public boolean isCollection(){
        return objectWrapper.isCollection();
    }

    public void add(Object element){
        objectWrapper.add(element);
    }

    public <E> void addAll(List<E> elements){
        objectWrapper.addAll(elements);
    }

}
