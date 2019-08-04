package com.zymonster.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author zhouyang
 * @date 2019/3/10
 */

@Table(name = "video_history")
@ApiModel(value = "视频观看历史",description = "这是视频观看历史")
public class VideoHistory {

    @Id
    private String id;

    @Column(name = "video_id")
    @ApiModelProperty(name = "观看视频的Id",value = "video_id")
    private String videoId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "visit_time")
    private Date visitTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(Date visitTime) {
        this.visitTime = visitTime;
    }
}
