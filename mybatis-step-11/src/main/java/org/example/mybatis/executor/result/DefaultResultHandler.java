package org.example.mybatis.executor.result;

import org.example.mybatis.reflection.factory.ObjectFactory;
import org.example.mybatis.session.ResultContext;
import org.example.mybatis.session.ResultHandler;

import java.util.ArrayList;
import java.util.List;

public class DefaultResultHandler implements ResultHandler {

    private final List<Object> list;

    public DefaultResultHandler(){
        this.list = new ArrayList<>();
    }

    /**
     * 通过 ObjectFactory 反射工具类，产生特定的 List
     */
    @SuppressWarnings("unchecked")
    public DefaultResultHandler(ObjectFactory objectFactory) {
        this.list = objectFactory.create(List.class);
    }


    @Override
    public void handleResult(ResultContext context) {
        list.add(context.getResultObject());
    }

    public List<Object> getResultList() {
        return list;
    }
}
