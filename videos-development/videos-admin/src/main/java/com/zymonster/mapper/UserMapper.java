package com.zymonster.mapper;

import com.zymonster.common.utils.MyMapper;
import com.zymonster.domain.Admin;
import com.zymonster.domain.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhouyang
 * @date 2019/3/15
 */
public interface UserMapper  {

    Admin getUserByUsername(String username);

    List<Role> getUserRolesByAdminId(@Param("userId") Integer id);
}
