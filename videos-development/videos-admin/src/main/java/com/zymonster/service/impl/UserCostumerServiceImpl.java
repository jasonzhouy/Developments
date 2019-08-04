package com.zymonster.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zymonster.common.utils.PagedResult;
import com.zymonster.domain.UserCostumer;
import com.zymonster.mapper.UserCostumerMapper;
import com.zymonster.service.UserCostumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zhouyang
 * @date 2019/3/28
 */

@Service
public class UserCostumerServiceImpl implements UserCostumerService {

    @Autowired
    private UserCostumerMapper userCostumerMapper;



    @Override
    public PagedResult getAllUserInfo(Integer page, Integer pageSize, String username) {

        PageHelper.startPage(page, pageSize);

        List<UserCostumer> list = userCostumerMapper.getAllUserInfo(username);

        PageInfo<UserCostumer> pageList = new PageInfo<>(list);

        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(page);
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRows(list);
        pagedResult.setRecords(pageList.getTotal());


        return pagedResult;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUser(String username) {
        userCostumerMapper.deleteUser(username);
    }
}
