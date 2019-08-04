package com.zymonster.service;

import com.zymonster.pojo.Bgm;
import com.zymonster.pojo.Tags;

import java.util.List;

/**
 * @author zhouyang
 * @date 2019/3/12
 */
public interface TagsService {

    /**
     * @Description: 查询背景音乐列表
     */
    public List<Tags> queryTagsList();

}
