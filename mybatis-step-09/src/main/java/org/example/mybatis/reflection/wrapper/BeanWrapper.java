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
    public Object get(PropertyTokenizer prop) {
        if (prop.getIndex() != null){
            Object collection = resolveCollection(prop, object);
            return getCollectionValue(prop, collection);
        }else {
            return getBeanProperty(prop, object);
        }
    }

    private Object getBeanProperty(PropertyTokenizer prop, Object object) {
        try {
            Invoker method = metaClass.getGetInvoker(prop.getName());
            return method.invoke(object, NO_ARGUMENTS);
        } catch (RuntimeException e) {
            throw e;
        }catch (Throwable t){
            throw new RuntimeException("Could not get property '" + prop.getName() + "' from " + object.getClass() + ".  Cause: " + t.toString(), t);
        }
    }

    @Override
    public void set(PropertyTokenizer prop, Object value) {
        if (prop.getIndex() != null){
            Object collection = resolveCollection(prop, object);
            setCollectionValue(prop, collection, value);
        }else{
            setBeanProperty(prop, object, value);
        }

    }

    private void setBeanProperty(PropertyTokenizer prop, Object object, Object value) {
        try {
            Invoker method = metaClass.getSetInvoker(prop.getName());
            Object[] params = {value};
            method.invoke(object, params);
        } catch (Exception t) {
            throw new RuntimeException("Could not set property '" + prop.getName() + "' of '" + object.getClass() + "' with value '" + value + "' Cause: " + t.toString(), t);
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
                return metaClass.getSetterType(name);
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
                return metaClass.getGetterType(name);
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
            if (metaClass.hasSetter(propertyTokenizer.getIndexedName())){
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
                    return metaValue.hasSetter(propertyTokenizer.getChildren());
                }
            }else {
                return false;
            }
        }else {
            return metaClass.hasGetter(name);
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
        }catch (Exception e){
            throw new RuntimeException("Cannot set value of property '" + name + "' because '" + name + "' is null and cannot be instantiated on instance of " + type.getName() + ". Cause:" + e.toString(), e);
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
    public <E> void addAll(List<E> element) {
        throw new UnsupportedOperationException();
    }
}
