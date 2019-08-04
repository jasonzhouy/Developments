package com.zymonster.controller.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhouyang
 * @date 2019/4/25
 */
@RestController
@RequestMapping
public class HelloController {

    @RequestMapping("/hello")
    public String getHello(){
        return "hello";
    }
}
