package com.zymonster.es.demo.controller.config;/**
 * @author zhouyang
 * @date 2019/7/22
 */

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

/**
 * @Author: zhouyang
 * @Date: 2019/7/22 19:40
 * @Description:
 **/
@Configuration
public class RestConfig {
    @Bean
    public RestClient getClient() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        // 如果有多个从节点可以持续在内部new多个HttpHost，参数1是ip,参数2是HTTP端口，参数3是通信协议
        RestClientBuilder clientBuilder = RestClient.builder(new HttpHost("localhost", 9200, "http"));

        // 添加其他配置，返回来的还是RestClientBuilder对象，这些配置都是可选的
        // clientBuilder.setXX()...
        Header[] defaultHeaders = {new BasicHeader("content-type", "application/json;")};
        clientBuilder.setDefaultHeaders(defaultHeaders);

        // 最后配置好的clientBuilder再build一下即可得到真正的Client
        return clientBuilder.build();
    }
}
