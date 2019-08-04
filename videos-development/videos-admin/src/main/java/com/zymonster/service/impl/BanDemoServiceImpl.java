package com.zymonster.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zymonster.common.utils.PagedResult;
import com.zymonster.domain.Ban.BanPOJO;
import com.zymonster.domain.BanDemo;
import com.zymonster.domain.UserCostumer;
import com.zymonster.mapper.BanListMapper;
import com.zymonster.service.BanDemoService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author zhouyang
 * @date 2019/4/26
 */
@Service
public class BanDemoServiceImpl implements BanDemoService {

    @Autowired
    private Sid sid;

    @Autowired
    private BanListMapper banListMapper;

    @Override
    public void addBanList(String userId, String reason, Date endTime, Date dealTime) {

        BanDemo banDemo = new BanDemo();
        banDemo.setId(sid.nextShort());
        banDemo.setDealUser("admin");
        banDemo.setDealTime(dealTime);
        banDemo.setReason(reason);
        banDemo.setUserId(userId);
        banDemo.setEndTime(endTime);

        banListMapper.addBanList(banDemo);

    }

    @Override
    public PagedResult getBanList(String username,Integer page,Integer pageSize) {

        PageHelper.startPage(page, pageSize);

        List<BanPOJO> list = banListMapper.getBanList(username);


        PageInfo<BanPOJO> pageList = new PageInfo<>(list);

        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(page);
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRows(list);
        pagedResult.setRecords(pageList.getTotal());


        return pagedResult;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void cancelBan(String id) {

        banListMapper.cancelBanList(id);
    }
}
