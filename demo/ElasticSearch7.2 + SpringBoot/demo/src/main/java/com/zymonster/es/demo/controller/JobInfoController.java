package com.zymonster.es.demo.controller;/**
 * @author zhouyang
 * @date 2019/7/22
 */


import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.ResponseListener;
import org.elasticsearch.client.RestClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @Author: zhouyang
 * @Date: 2019/7/22 19:41
 * @Description:
 **/
@RestController
@RequestMapping("/rest")
public class JobInfoController {
    @Autowired
    private RestClient client;

//    // RequestOptions类保存应在同一应用程序中的多个请求之间共享的部分请求
//    private static final RequestOptions COMMON_OPTIONS;
//
//    static {
//        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
//        // 添加所有请求所需的任何标头。
//        builder.addHeader("Authorization", "Bearer " + TOKEN);
//        // 自定义响应使用者
//        builder.setHttpAsyncResponseConsumerFactory(
//                new HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory(30 * 1024 * 1024 * 1024));
//        COMMON_OPTIONS = builder.build();
//    }

    @RequestMapping(value = "/go", method = RequestMethod.GET)
    public ResponseEntity<String> go() {
        return new ResponseEntity<>("go", HttpStatus.OK);
    }


    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<String> asynchronous() throws IOException {
        Request request = new Request(
                "GET",
                "/boss/job/_search");
       request.addParameter("pretty","true");
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObject1 = new JSONObject();
        JSONObject jsonObject2 = new JSONObject();
        jsonObject1.put("match_all",jsonObject2);
        jsonObject.put("query",jsonObject1);
        System.out.println(jsonObject);
        request.setEntity(new NStringEntity(jsonObject.toString()));

        Response response = client.performRequest(request);

        // 获取返回的内容
        String responseBody = EntityUtils.toString(response.getEntity());

        return new ResponseEntity<>(responseBody, HttpStatus.OK);

    }


}
