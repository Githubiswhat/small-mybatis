package org.example.mybatis.reflection.wrapper;

import org.example.mybatis.reflection.MetaClass;
import org.example.mybatis.reflection.MetaObject;
import org.example.mybatis.reflection.SystemMetaObject;
import org.example.mybatis.reflection.factory.ObjectFactory;
import org.example.mybatis.reflection.invoker.Invoker;
import org.example.mybatis.reflection.property.PropertyTokenizer;

import java.util.List;

public class BeanWrapper extends BaseWrapper{

    private Object object;

    private MetaClass metaClass;

    public BeanWrapper(MetaObject metaObject, Object object) {
        super(metaObject);
        this.object = object;
        this.metaClass = MetaClass.forClass(object.getClass());
    }

    @Override
    public Object get(PropertyTokenizer propertyTokenizer) {
        if (propertyTokenizer.getIndex() != null){
            Object collection = resolveCollection(propertyTokenizer, object);
            return getCollectionValue(propertyTokenizer, collection);
        }else {
            return getBeanProperty(propertyTokenizer, object);
        }
    }

    private Object getBeanProperty(PropertyTokenizer propertyTokenizer, Object object) {
        try {
            Invoker method =  metaClass.getGetInvoker(propertyTokenizer.getName());
            return method.invoke(object, NO_ARGUMENTS);
        } catch (RuntimeException e) {
            throw e;
        }catch (Throwable throwable){
            throw new RuntimeException("Could not get property '" + propertyTokenizer.getName() + "' from " + object.getClass() + ". Cause: " + throwable.toString(), throwable);
        }
    }

    @Override
    public void set(PropertyTokenizer propertyTokenizer, Object value) {
        if (propertyTokenizer.getIndex() != null){
            Object collection = resolveCollection(propertyTokenizer, object);
            setCollectionValue(propertyTokenizer, collection, value);
        }else{
            setBeanProperty(propertyTokenizer, object, value);
        }
    }

    private void setBeanProperty(PropertyTokenizer propertyTokenizer, Object object, Object value) {
        try {
            Invoker method = metaClass.getSetterInvoker(propertyTokenizer.getName());
            Object[] params = {value};
            method.invoke(object, params);
        } catch (Exception e) {
            throw new RuntimeException("Could not set Property '" + propertyTokenizer.getName() + "' of '" + object.getClass() + "' with value '" + value + "' Cause: " + e.toString(), e);
        }
    }

    @Override
    public String findProperty(String name, boolean useCamelCaseMapping) {
        return metaClass.findProperty(name, useCamelCaseMapping);
    }

    @Override
    public String[] getGetterNames() {
        return metaClass.getGetterNames();
    }

    @Override
    public String[] getSetterNames() {
        return metaClass.getSetterNames();
    }

    @Override
    public Class<?> getSetterType(String name) {
        PropertyTokenizer propertyTokenizer = new PropertyTokenizer(name);
        if (propertyTokenizer.hasNext()){
            MetaObject metaValue = metaObject.metaObjectForProperty(propertyTokenizer.getIndexedName());
            if (metaValue == SystemMetaObject.NULL_META_OBJECT){
                return metaValue.getSetterType(name);
            }else {
                return metaValue.getSetterType(propertyTokenizer.getChildren());
            }
        }else {
            return metaClass.getSetterType(name);
        }
    }

    @Override
    public Class<?> getGetterType(String name) {
        PropertyTokenizer propertyTokenizer = new PropertyTokenizer(name);
        if (propertyTokenizer.hasNext()){
            MetaObject metaValue = metaObject.metaObjectForProperty(propertyTokenizer.getIndexedName());
            if (metaValue == SystemMetaObject.NULL_META_OBJECT){
                return metaValue.getGetterType(name);
            }else {
                return metaValue.getGetterType(propertyTokenizer.getChildren());
            }
        }else {
            return metaClass.getGetterType(name);
        }
    }

    @Override
    public boolean hasGetter(String name) {
        PropertyTokenizer propertyTokenizer = new PropertyTokenizer(name);
        if (propertyTokenizer.hasNext()){
            if (metaClass.hasGetter(propertyTokenizer.getIndexedName())){
                MetaObject metaValue = metaObject.metaObjectForProperty(propertyTokenizer.getIndexedName());
                if (metaValue == SystemMetaObject.NULL_META_OBJECT){
                    return metaClass.hasGetter(name);
                }else {
                    return metaValue.hasGetter(propertyTokenizer.getChildren());
                }
            }else {
                return false;
            }
        }else {
            return metaClass.hasGetter(name);
        }
    }

    @Override
    public boolean hasSetter(String name) {
        PropertyTokenizer propertyTokenizer = new PropertyTokenizer(name);
        if (propertyTokenizer.hasNext()){
            if (metaClass.hasSetter(propertyTokenizer.getIndexedName())){
                MetaObject metaValue = metaObject.metaObjectForProperty(propertyTokenizer.getIndexedName());
                if (metaValue == SystemMetaObject.NULL_META_OBJECT){
                    return metaClass.hasSetter(name);
                }else {
                    return metaObject.hasSetter(propertyTokenizer.getChildren());
                }
            }else {
                return false;
            }
        }else {
            return metaClass.hasSetter(name);
        }
    }

    @Override
    public MetaObject instantiatePropertyValue(String name, PropertyTokenizer propertyTokenizer, ObjectFactory objectFactory) {
        MetaObject metaValue;
        Class<?> type = getSetterType(propertyTokenizer.getName());
        try {
            Object newObject = objectFactory.create(type);
            metaValue = MetaObject.forObject(newObject, metaObject.getObjectFactory(), metaObject.getObjectWrapperFactory());
            set(propertyTokenizer, newObject);
        } catch (Exception e) {
            throw new RuntimeException("Cannot set value of property '" + name + "' because '" + name + "' is null and cannot be instantiated on instance of " + type.getName() + ". Cause: " + e.toString(), e);
        }
        return metaValue;
    }

    @Override
    public boolean isCollection() {
        return false;
    }

    @Override
    public void add(Object element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <E> void addAll(List<E> elements) {
        throw new UnsupportedOperationException();
    }
}
