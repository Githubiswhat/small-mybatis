package org.example.mybatis.test.dao;

import org.example.mybatis.test.po.Activity;

public interface IActivityDao {

    Activity queryActivityById(Activity activity);

    Integer insert(Activity activity);

}
