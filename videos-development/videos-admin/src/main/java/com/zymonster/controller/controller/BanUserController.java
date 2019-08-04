package com.zymonster.controller.controller;

import com.zymonster.common.utils.PagedResult;
import com.zymonster.common.utils.ResultJSON;
import com.zymonster.service.BanDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.transform.Result;

/**
 * @author zhouyang
 * @date 2019/4/26
 */

@RestController
@RequestMapping("/banUser")
public class BanUserController {

    @Autowired
    private BanDemoService banDemoService;

    @GetMapping("/list")
    public ResultJSON getBanList(String username, Integer page) {
        if (page == null) {
            page = 1;
        }

        int pageSize = 10;

        PagedResult pagedResult = banDemoService.getBanList(username, page, pageSize);


        return ResultJSON.ok(pagedResult);
    }

    ;

    @GetMapping("/cancelBan")
    public ResultJSON cancelBanList(String id) throws Exception {
        System.out.println(id);
        banDemoService.cancelBan(id);

        return ResultJSON.ok();
    }

    ;
}
