package com.zymonster.service;

import com.zymonster.common.utils.PagedResult;
import io.swagger.models.auth.In;

/**
 * @author zhouyang
 * @date 2019/4/20
 */
public interface VideoService {

    PagedResult getAllVideoList(Integer page, Integer pageSize,String username);

    void deleteVideo(String id);

    PagedResult getAllReportList(Integer page,Integer pageSize);

    void offlineVideo(String id,String username,String videoId);

    void cancelVideo(String id,String videoId,String username);
}
