package com.zymonster.mapper;

import com.zymonster.domain.Report;
import com.zymonster.domain.Video;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhouyang
 * @date 2019/4/20
 */
public interface VideoMapper {

    List<Video> getAllVideoList(@Param("username")String username);

    void deleteVideo(@Param("id")String id);

    void deleteVideoHistory(@Param("id")String id);

    void deleteUserLikeVideos(@Param("id")String id);

    Video getPath(@Param("id") String id);

    List<Report> getReportList();

    void deleteReport(@Param("id")String id);

    void offlineVideo(@Param("id") String videoId);

    void onlineVideo(@Param("id")String videoId);
}
