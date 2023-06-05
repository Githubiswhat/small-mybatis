package org.example.mybatis.reflection.wrapper;

import org.example.mybatis.reflection.MetaObject;

public interface ObjectWrapperFactory {

    boolean hasWrapperFor(Object object);

    ObjectWrapper getWrapperFor(MetaObject metaObject, Object object);

}
