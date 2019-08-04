package com.zymonster.service;

import com.zymonster.common.utils.PagedResult;
import io.swagger.models.auth.In;

import java.util.Date;

/**
 * @author zhouyang
 * @date 2019/4/26
 */
public interface BanDemoService {

    void addBanList(String userId, String reason, Date endTime,Date dealTime);

    PagedResult getBanList(String username, Integer page, Integer pageSie);

    void cancelBan(String id);
}
