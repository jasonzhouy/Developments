package com.zymonster.controller.controller;

import com.zymonster.common.utils.PagedResult;
import com.zymonster.common.utils.ResultJSON;
import com.zymonster.service.BanDemoService;
import com.zymonster.service.UserCostumerService;
import com.zymonster.service.UserServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zhouyang
 * @date 2019/3/28
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserCostumerService userCostumerService;

    @Autowired
    private BanDemoService banDemoServiceImpl;


    @GetMapping("/getUserInfo")
    public ResultJSON getUserInfo(Integer page, String username) throws Exception {
        if (page == null) {
            page = 1;
        }

        int pageSize = 10;

        PagedResult pagedResult = userCostumerService.getAllUserInfo(page, pageSize, username);
        System.out.println("ok");

        return ResultJSON.ok(pagedResult);
    }

    @GetMapping("/remove")
    public ResultJSON removeUser(String username) throws Exception {
        userCostumerService.deleteUser(username);
        return ResultJSON.ok();
    }

    @GetMapping("/add")
    public ResultJSON addUser(String username, String password, String mailAddress) throws Exception {
        System.out.println(username);
        System.out.println(password);
        System.out.println(mailAddress);
        return ResultJSON.ok();
    }

    @GetMapping("/forbid")
    public ResultJSON forbidUser(String id, String endTime,String dealTime,String reason) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date dateEnd = sdf.parse(endTime);
        Date dateDeal = sdf.parse(dealTime);
        String userId = id;

        banDemoServiceImpl.addBanList(userId,reason,dateEnd,dateDeal);

        return ResultJSON.ok();
    }
}
