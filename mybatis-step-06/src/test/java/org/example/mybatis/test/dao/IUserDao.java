package org.example.mybatis.test.dao;

import org.example.mybatis.test.po.User;

public interface IUserDao {

    User queryUserInfoById(Long uId);

}
