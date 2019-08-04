package com.zymonster.controller.controller;

import com.zymonster.common.utils.PagedResult;
import com.zymonster.common.utils.ResultJSON;
import com.zymonster.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhouyang
 * @date 2019/4/20
 */

@RestController
@RequestMapping("/video")
public class VideoController {

    @Autowired
    private VideoService videoServiceImpl;

    @GetMapping("/getAll")
    public ResultJSON getAllVideoList(Integer page,@RequestParam("name") String username) throws Exception{
        if (page == null){
            page = 1;
        }

        int pageSize = 5;


        System.out.println(username);

        PagedResult pagedResult =  videoServiceImpl.getAllVideoList(page,pageSize,username);

        return ResultJSON.ok(pagedResult);
    }

    @GetMapping("/batchRemove")
    public ResultJSON batchRemoveVideos(String ids) throws Exception{

        String[] input = ids.split(",");

        for (String i: input
             ) {

            System.out.println(i);
            videoServiceImpl.deleteVideo(i);

        }



        return ResultJSON.ok();
    }


    @GetMapping("/reportList")
    public ResultJSON reportList(Integer page) throws Exception{

        if (page == null){
            page = 1;
        }

        int pageSize = 5;



        PagedResult pagedResult =  videoServiceImpl.getAllReportList(page,pageSize);
        return ResultJSON.ok(pagedResult);


    }

    @GetMapping("/offlineReport")
    public ResultJSON offlineReport(String id,String username,String videoId) throws Exception{

        videoServiceImpl.offlineVideo(id,username,videoId);

        return ResultJSON.ok();

    }


    @GetMapping("/cancelReport")
    public ResultJSON cancelReport(String id,String videoId,String username) throws Exception{


        videoServiceImpl.cancelVideo(id,videoId,username);

        return ResultJSON.ok();

    }



}
