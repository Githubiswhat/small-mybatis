package org.example.mybatis.reflection.wrapper;

import org.example.mybatis.reflection.MetaObject;
import org.example.mybatis.reflection.SystemMetaObject;
import org.example.mybatis.reflection.factory.ObjectFactory;
import org.example.mybatis.reflection.property.PropertyTokenizer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapWrapper extends BaseWrapper{

    private Map<String, Object> map;

    public MapWrapper(MetaObject metaObject, Map map) {
        super(metaObject);
        this.map = map;
    }

    @Override
    public Object get(PropertyTokenizer propertyTokenizer) {
        if (propertyTokenizer.getIndex() != null){
            Object collection = resolveCollection(propertyTokenizer, map);
            return getCollectionValue(propertyTokenizer, collection);
        }else {
            return map.get(propertyTokenizer.getName());
        }
    }

    @Override
    public void set(PropertyTokenizer propertyTokenizer, Object value) {
        if (propertyTokenizer.getIndex() != null){
            Object collection = resolveCollection(propertyTokenizer, map);
            setCollectionValue(propertyTokenizer, collection, value);
        }else {
            map.put(propertyTokenizer.getName(), value);
        }
    }

    @Override
    public String findProperty(String name, boolean useCamelCaseMapping) {
        return null;
    }

    @Override
    public String[] getGetterNames() {
        return map.keySet().toArray(new String[map.keySet().size()]);
    }

    @Override
    public String[] getSetterNames() {
        return map.keySet().toArray(new String[map.keySet().size()]);
    }

    @Override
    public Class<?> getSetterType(String name) {
        PropertyTokenizer propertyTokenizer = new PropertyTokenizer(name);
        if (propertyTokenizer.hasNext()){
            MetaObject metaValue = metaObject.metaObjectForProperty(propertyTokenizer.getName());
            if (metaValue == SystemMetaObject.NULL_META_OBJECT){
                return Object.class;
            }else {
                return metaValue.getSetterType(propertyTokenizer.getChildren());
            }
        }else {
            if (map.get(name) != null){
                return map.get(name).getClass();
            }else {
                return Object.class;
            }
        }
    }

    @Override
    public Class<?> getGetterType(String name) {
        PropertyTokenizer propertyTokenizer = new PropertyTokenizer(name);
        if (propertyTokenizer.hasNext()){
            MetaObject metaValue = metaObject.metaObjectForProperty(propertyTokenizer.getName());
            if (metaValue == SystemMetaObject.NULL_META_OBJECT){
                return Object.class;
            }else {
                return metaValue.getGetterType(propertyTokenizer.getChildren());
            }
        }else {
            if (map.get(name) != null){
                return map.get(name).getClass();
            }else {
                return Object.class;
            }
        }
    }

    @Override
    public boolean hasGetter(String name) {
        PropertyTokenizer propertyTokenizer = new PropertyTokenizer(name);
        if (propertyTokenizer.hasNext()){
            if (map.containsKey(propertyTokenizer.getIndexedName())){
                MetaObject metaValue = metaObject.metaObjectForProperty(propertyTokenizer.getIndexedName());
                if (metaValue == SystemMetaObject.NULL_META_OBJECT){
                    return true;
                }else {
                    return metaValue.hasGetter(propertyTokenizer.getChildren());
                }
            }else {
                return false;
            }
        }else {
            return map.containsKey(propertyTokenizer.getName());
        }
    }

    @Override
    public boolean hasSetter(String name) {
        return true;
    }

    @Override
    public MetaObject instantiatePropertyValue(String name, PropertyTokenizer propertyTokenizer, ObjectFactory objectFactory) {
        HashMap<String, Object> map = new HashMap<>();
        set(propertyTokenizer, map);
        return MetaObject.forObject(map, metaObject.getObjectFactory(), metaObject.getObjectWrapperFactory());
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
