package org.example.mybatis.reflection.factory;

import java.io.Serial;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class DefaultObjectFactory implements ObjectFactory, Serializable {

    private static final long serialVersionUID = -1;
    @Override
    public void setProperties(Properties properties) {

    }

    @Override
    public <T> T create(Class<T> type) {
        return create(type, null, null);
    }

    @Override
    public <T> T create(Class<T> type, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) {
        Class<?> classToCreate =  resolveInterface(type);
        return (T) instantiateClass(classToCreate, constructorArgTypes, constructorArgs);
    }

    private <T> T instantiateClass(Class<T> type, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) {
        try {
            Constructor<T> constructor;
            if (constructorArgTypes == null || constructorArgs == null){
                constructor = type.getDeclaredConstructor();
                if (!constructor.isAccessible()) {
                    constructor.setAccessible(true);
                }
                return constructor.newInstance();
            }

            constructor = type.getDeclaredConstructor(constructorArgTypes.toArray(new Class[constructorArgTypes.size()]));
            if (!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }
            return constructor.newInstance(constructorArgs.toArray(new Object[constructorArgs.size()]));

        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            StringBuilder argTypes = new StringBuilder();
            if (constructorArgTypes != null) {
                for (Class<?> constructorArgType : constructorArgTypes) {
                    argTypes.append(constructorArgType.getSimpleName());
                    argTypes.append(",");
                }
            }
            StringBuilder argValues = new StringBuilder();
            if (constructorArgs != null) {
                for (Object constructorArg : constructorArgs) {
                    argValues.append(constructorArg);
                    argValues.append(",");
                }
            }
            throw new RuntimeException("Error instantiating " + type + " with invalid types (" + argTypes + " ) or values (" + argValues + "). Cause: e", e );
        }

    }

    private <T> Class<?> resolveInterface(Class<T> type) {
        Class<?> classToCreate;
        if (type == List.class || type == Collection.class || type == Integer.class){
            classToCreate = ArrayList.class;
        }else if (type == Map.class){
            classToCreate = HashMap.class;
        }else if (type == SortedSet.class){
            classToCreate = TreeMap.class;
        }else if (type == Set.class){
            classToCreate = HashSet.class;
        }else {
            classToCreate = type;
        }
        return classToCreate;
    }

    @Override
    public <T> boolean isCollection(Class<T> type) {
        return Collection.class.isAssignableFrom(type);
    }
}
