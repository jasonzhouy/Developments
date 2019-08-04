package com.zymonster.service.impl;

import com.zymonster.mapper.BgmMapper;
import com.zymonster.mapper.TagsMapper;
import com.zymonster.pojo.Bgm;
import com.zymonster.pojo.Tags;
import com.zymonster.service.TagsService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zhouyang
 * @date 2019/3/12
 */

@Service
public class TagsServiceImpl implements TagsService {

    @Autowired
    private TagsMapper tagsMapper;

    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Tags> queryTagsList() {
        return tagsMapper.selectAll();
    }
}
