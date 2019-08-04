package com.zymonster.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zymonster.common.utils.PagedResult;
import com.zymonster.domain.Bgm;
import com.zymonster.domain.Report;
import com.zymonster.domain.Video;
import com.zymonster.mapper.UserCostumerMapper;
import com.zymonster.mapper.VideoMapper;
import com.zymonster.service.VideoService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author zhouyang
 * @date 2019/4/20
 */

@Service
public class VideoServiceImpl implements VideoService
{

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private UserCostumerMapper userCostumerMapper;

    @Override
    public PagedResult getAllVideoList(Integer page, Integer pageSize,String username) {


        PageHelper.startPage(page, pageSize);

        List<Video> list = videoMapper.getAllVideoList(username);

        PageInfo<Video> pageList = new PageInfo<>(list);

        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(page);
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRows(list);
        pagedResult.setRecords(pageList.getTotal());


        return pagedResult;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteVideo(String id) {

        Video video = videoMapper.getPath(id);

        videoMapper.deleteVideo(id);
        videoMapper.deleteUserLikeVideos(id);
        videoMapper.deleteVideoHistory(id);


        File file1 = new File(video.getCoverPath());
        File file2 = new File(video.getVideoPath());
        try {
            FileUtils.forceDelete(file1);
            FileUtils.forceDelete(file2);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedResult getAllReportList(Integer page, Integer pageSize) {

        PageHelper.startPage(page, pageSize);

        List<Report> list = videoMapper.getReportList();

        PageInfo<Report> pageList = new PageInfo<>(list);

        PagedResult pagedResult = new PagedResult();
        pagedResult.setPage(page);
        pagedResult.setTotal(pageList.getPages());
        pagedResult.setRows(list);
        pagedResult.setRecords(pageList.getTotal());


        return pagedResult;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void offlineVideo(String id, String username, String videoId) {
        videoMapper.offlineVideo(videoId);
        userCostumerMapper.reduceCreditPoints(username);
        videoMapper.deleteReport(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void cancelVideo(String id, String videoId,String username) {
        videoMapper.onlineVideo(videoId);
        //userCostumerMapper.addCreditPoints(username);
        videoMapper.deleteReport(id);

    }
}
