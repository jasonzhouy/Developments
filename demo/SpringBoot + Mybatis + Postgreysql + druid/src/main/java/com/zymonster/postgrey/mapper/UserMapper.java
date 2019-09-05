package com.zymonster.postgrey.mapper;

import com.zymonster.postgrey.domain.DO.User;

import java.util.List;

/**
 * @author zhouyang
 * @date 2019/9/5
 */
public interface UserMapper {

    /**
     * 添加用户
     */
    void addUserInfo(User user);

    /**
     * 获取用户列表
     */

    List<User> getUserList();
}
