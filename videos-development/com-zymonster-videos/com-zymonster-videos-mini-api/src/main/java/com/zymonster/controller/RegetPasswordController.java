package com.zymonster.controller;

import com.zymonster.utils.CryptoUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * @author zhouyang
 * @date 2019/3/11
 */

@Controller
@RequestMapping("/password")
public class RegetPasswordController {

    @GetMapping("/userForgetPassword")
    @ApiOperation(value = "用户找回密码", notes = "用户找回密码接口")
    public String userForgetPassword(String username, String date, Map<String, Object> map) throws ParseException {

        System.out.println(username);
        String deUsername = CryptoUtil.decode(username);
        String deDate = CryptoUtil.decode(date);
        System.out.println(deDate);
        SimpleDateFormat sf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        Date come = sf.parse(deDate);
        Date current = new Date();
        long result = current.getTime() - come.getTime();
        long waitTime = 1000 * 60 * 15;
        if (result > waitTime) {
            map.put("msg", "超出时间，请重新申请");
            return "error";
        } else if (result < 0) {
            map.put("msg", "非法时间！");
            return "error";
        } else {
            map.put("username", deUsername);
            return "password";

        }

    }


    @PostMapping("/dosubmit")
    @ResponseBody
    public String dosubmit() {
        return "success";
    }

}
