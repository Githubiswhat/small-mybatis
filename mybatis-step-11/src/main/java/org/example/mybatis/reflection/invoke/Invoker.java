package org.example.mybatis.reflection.invoke;

public interface Invoker {

    Object invoke(Object target, Object[] args) throws Exception;

    Class<?> getType();
}
