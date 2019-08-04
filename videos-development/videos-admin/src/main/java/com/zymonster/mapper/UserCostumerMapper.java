package com.zymonster.mapper;

import com.zymonster.domain.UserCostumer;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

/**
 * @author zhouyang
 * @date 2019/3/28
 */
public interface UserCostumerMapper {

    List<UserCostumer> getAllUserInfo(@Param(value = "username") String username);

    void deleteUser(@Param(value = "username")String username);

    void reduceCreditPoints(@Param(value = "username") String username);

    void addCreditPoints(@Param(value = "username") String username);
}
