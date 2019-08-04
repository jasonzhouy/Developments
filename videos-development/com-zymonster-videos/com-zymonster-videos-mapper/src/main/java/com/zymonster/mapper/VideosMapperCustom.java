package com.zymonster.mapper;

import com.zymonster.pojo.Videos;
import com.zymonster.pojo.vo.VideosVO;
import com.zymonster.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VideosMapperCustom extends MyMapper<Videos> {

    /**
     * @Description: 条件查询所有视频列表
     */
    public List<VideosVO> queryAllVideos(@Param("videoDesc") String videoDesc,
                                         @Param("userId") String userId);

    /**
     * @Description: 查询关注的视频
     */
    public List<VideosVO> queryMyFollowVideos(String userId);

    /**
     * @Description: 查询点赞视频
     */
    public List<VideosVO> queryMyLikeVideos(@Param("userId") String userId);

    /**
     * @Description: 对视频喜欢的数量进行累加
     */
    public void addVideoLikeCount(String videoId);

    /**
     * @Description: 对视频喜欢的数量进行累减
     */
    public void reduceVideoLikeCount(String videoId);

    /**
     * @Description: 查询单个视频信息，根据视频Id
     */
    public VideosVO queryOneVideoInfo(@Param("videoId") String videoId);

    /**
     * @Description: 查询除了当前浏览的视频之外的其他视频列表
     */
    public List<String> queryVideoList(@Param("videoId") String videoId);


    /**
     * @Description: 查询所有推荐视频列表
     */
    public List<VideosVO> queryAllRecommandVideos(
            @Param("tagsId") String tagsId
    );

    /**
     * @Description: 查询所有推荐视频列表
     */

    public void deleteVideoInfo(@Param("videoId")String videoId);
}