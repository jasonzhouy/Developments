package com.zymonster.service;

import com.zymonster.common.utils.PagedResult;
import com.zymonster.domain.Bgm;

/**
 * @author zhouyang
 * @date 2019/4/8
 */
public interface BgmService {

    PagedResult getAllUserInfo(Integer page, Integer pageSize);

    void deleteBgm(String id);

    void addBgm(Bgm bgm);
}
