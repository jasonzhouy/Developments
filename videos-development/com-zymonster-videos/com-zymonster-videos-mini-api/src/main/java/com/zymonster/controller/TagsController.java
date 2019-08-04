package com.zymonster.controller;

import com.zymonster.pojo.Tags;
import com.zymonster.service.TagsService;
import com.zymonster.utils.ResultJSON;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhouyang
 * @date 2019/3/12
 */

@RestController
@RequestMapping("/tags")
public class TagsController {

    @Autowired
    private TagsService tagsService;

    @GetMapping("/list")
    @ApiOperation(value="获取标签列表", notes="获取标签列表的接口")
    public ResultJSON getTagsList(){

        List<Tags> result = tagsService.queryTagsList();
        return ResultJSON.ok(result);
    }
}
