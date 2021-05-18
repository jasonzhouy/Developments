package com.zymonster.es.demo.dao;/**
 * @author zhouyang
 * @date 2019/9/1
 */

import com.zymonster.es.demo.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author: zhouyang
 * @Date: 2019/9/1 13:24
 * @Description:
 **/

public interface UserDao extends JpaRepository<User, String> {

    List<User> findAll();

}
