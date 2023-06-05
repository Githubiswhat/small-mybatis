package org.example.mybatis.reflection.wrapper;

import org.example.mybatis.reflection.MetaObject;
import org.example.mybatis.reflection.property.PropertyTokenizer;

import java.util.List;
import java.util.Map;

public abstract class BaseWrapper implements ObjectWrapper{

    protected static final Object[] NO_ARGUMENTS = new Object[0];

    protected MetaObject metaObject;

    public BaseWrapper(MetaObject metaObject) {
        this.metaObject = metaObject;
    }

    protected Object resolveCollection(PropertyTokenizer propertyTokenizer, Object object){
        if ("".equals(propertyTokenizer.getName())){
            return object;
        }else {
            return metaObject.getValue(propertyTokenizer.getName());
        }
    }

    protected Object getCollectionValue(PropertyTokenizer propertyTokenizer, Object collection){
        if (collection instanceof Map){
            return ((Map) collection).get(propertyTokenizer.getIndexedName());
        }else {
            int i = Integer.parseInt(propertyTokenizer.getIndex());
            if (collection instanceof List){
                return ((List) collection).get(i);
            }else if (collection instanceof Object[]){
                return ((Object[]) collection)[i];
            }else if (collection instanceof char[]){
                return ((char[]) collection)[i];
            }else if (collection instanceof boolean[]){
                return ((boolean[]) collection)[i];
            }else if (collection instanceof  byte[]){
                return ((byte[]) collection)[i];
            }else if (collection instanceof double[]){
                return ((double[]) collection)[i];
            }else if (collection instanceof float[]){
                return ((float[]) collection)[i];
            }else if (collection instanceof int[]){
                return ((int[]) collection)[i];
            }else if (collection instanceof long[]){
                return ((long[]) collection)[i];
            }else if (collection instanceof short[]){
                return ((short[]) collection)[i];
            }else {
                throw new RuntimeException("The '" + propertyTokenizer.getName() + "' property of " + collection + " is not a List or Array.");
            }
        }
    }

    protected void setCollectionValue(PropertyTokenizer propertyTokenizer, Object collection, Object value){
        if (collection instanceof Map){
            ((Map) collection).put(propertyTokenizer.getIndex(), value);
        }else {
            int i = Integer.parseInt(propertyTokenizer.getIndex());
            if (collection instanceof List){
                ((List) collection).set(i, value);
            }else if (collection instanceof Object[]){
                ((Object[]) collection)[i] = value;
            }else if (collection instanceof char[]){
                ((char[]) collection)[i] = (char) value;
            }else if (collection instanceof boolean[]){
                ((boolean[]) collection)[i] = (boolean) value;
            }else if (collection instanceof  byte[]){
                ((byte[]) collection)[i] = (byte) value;
            }else if (collection instanceof double[]){
                ((double[]) collection)[i] = (double) value;
            }else if (collection instanceof float[]){
                ((float[]) collection)[i] = (float) value;
            }else if (collection instanceof int[]){
                ((int[]) collection)[i] = (int) value;
            }else if (collection instanceof long[]){
                ((long[]) collection)[i] = (long) value;
            }else if (collection instanceof short[]){
                ((short[]) collection)[i] = (short) value;
            }else {
                throw new RuntimeException("The '" + propertyTokenizer.getName() + "' property of " + collection + " is not a List or Array");
            }
        }
    }
}
