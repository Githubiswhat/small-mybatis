package org.example.mybatis.reflection.wrapper;

import org.example.mybatis.reflection.MetaObject;

public class DefaultObjectWrapperFactory implements ObjectWrapperFactory{

    @Override
    public boolean hasWrapper(Object object) {
        return false;
    }

    @Override
    public ObjectWrapper getWrapperFor(MetaObject metaObject, Object object) {
        throw new RuntimeException("The DefaultObjectWrapperFactory should never be called to provide an ObjectWrapper");
    }
}
