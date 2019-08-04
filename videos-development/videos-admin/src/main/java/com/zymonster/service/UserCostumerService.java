package com.zymonster.service;

import com.zymonster.common.utils.PagedResult;

/**
 * @author zhouyang
 * @date 2019/3/28
 */
public interface UserCostumerService {

    /*
      @description: 分页查询所有用户列表信息
     */
    PagedResult getAllUserInfo(Integer page,Integer pageSize,String username);

    void deleteUser(String username);
}
