package com.zymonster.service;

import com.zymonster.domain.Admin;
import com.zymonster.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author zhouyang
 * @date 2019/3/15
 */

@Service
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = userMapper.getUserByUsername(username);

        if(admin == null){
            throw new UsernameNotFoundException("用户不存在!");
        }
        admin.setRoles(userMapper.getUserRolesByAdminId(admin.getId()));
        System.out.println(admin.getUsername());
        return admin;
    }
}
