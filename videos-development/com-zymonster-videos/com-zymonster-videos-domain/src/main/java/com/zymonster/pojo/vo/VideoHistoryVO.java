package com.zymonster.pojo.vo;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author zhouyang
 * @date 2019/3/12
 */
public class VideoHistoryVO {



    @Column(name = "user_id")
    private String userId;


    @Column(name = "tags_id")
    private String tagsId;

    @Column(name = "number")
    private  Integer number;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTagsId() {
        return tagsId;
    }

    public void setTagsId(String tagsId) {
        this.tagsId = tagsId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}