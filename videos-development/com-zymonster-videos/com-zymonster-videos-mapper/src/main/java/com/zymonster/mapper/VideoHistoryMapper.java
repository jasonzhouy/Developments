package com.zymonster.mapper;

import com.zymonster.pojo.VideoHistory;
import com.zymonster.pojo.vo.VideoHistoryVO;
import com.zymonster.utils.MyMapper;

import javax.validation.constraints.Max;
import java.util.List;

/**
 * @author zhouyang
 * @date 2019/3/10
 */
public interface VideoHistoryMapper extends MyMapper<VideoHistory> {

    /*
        author:zhou
        description:查询视频浏览记录
     */

    List<VideoHistory> getVideoAllHistory(String videoId);

    /*
        author:zhou
        description:查询视频浏览记录
     */

    List<VideoHistoryVO> getUserAllVideoHistory(String userId);





}
