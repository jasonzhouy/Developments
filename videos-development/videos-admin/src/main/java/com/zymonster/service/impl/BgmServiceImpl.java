package com.zymonster.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zymonster.common.enums.BGMOperatorTypeEnum;
import com.zymonster.common.utils.JsonUtils;
import com.zymonster.common.utils.PagedResult;
import com.zymonster.controller.ZKCurator;
import com.zymonster.domain.Bgm;
import com.zymonster.mapper.BgmMapper;
import com.zymonster.service.BgmService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhouyang
 * @date 2019/4/8
 */

@Service
public class BgmServiceImpl implements BgmService {

    @Autowired
    private BgmMapper bgmMapper;

    @Autowired
    private ZKCurator zkCurator;


    @Autowired
    private Sid sid;


    @Override
    public PagedResult getAllUserInfo(Integer page, Integer pageSize) {

        PageHelper.startPage(page, pageSize);

        List<Bgm> list = bgmMapper.getBgmList();

        PageInfo<Bgm> pageList = new PageInfo<>(list);

        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(page);
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRows(list);
        pagedResult.setRecords(pageList.getTotal());


        return pagedResult;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteBgm(String id) {

        Bgm bgm = bgmMapper.getBgmById(id);

        bgmMapper.deleteBgm(id);

        Map<String, String> map = new HashMap<>();
        map.put("operType", BGMOperatorTypeEnum.DELETE.type);
        map.put("path", bgm.getPath());

        zkCurator.sendBgmOperator(id, JsonUtils.objectToJson(map));

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void addBgm(Bgm bgm) {

        String bgmId = sid.nextShort();
        bgm.setId(bgmId);
        bgmMapper.addBgm(bgm);

        Map<String, String> map = new HashMap<>();
        map.put("operType", BGMOperatorTypeEnum.ADD.type);
        map.put("path", bgm.getPath());

        zkCurator.sendBgmOperator(bgmId, JsonUtils.objectToJson(map));



    }
}
