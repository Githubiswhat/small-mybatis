package org.example.mybatis.reflection.wrapper;

import org.example.mybatis.reflection.MetaObject;

public interface ObjectWrapperFactory {

    boolean hasWrapper(Object object);

    ObjectWrapper getWrapperFor(MetaObject metaObject, Object object);

}
