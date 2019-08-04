package com.zymonster.controller;

import com.zymonster.controller.config.ResourceConfig;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author zhouyang
 * @date 2019/4/19
 */

public class ZKCurator {

    // zk客户端
    private CuratorFramework client = null;
    final static Logger log = LoggerFactory.getLogger(ZKCurator.class);

//	@Autowired
//	private BgmService bgmService;

//	public static final String ZOOKEEPER_SERVER = "192.168.1.210:2181";

    @Autowired
    private ResourceConfig resourceConfig;

    public void init() {

        if (client != null) {
            return;
        }

        // 重试策略
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);
        // 创建zk客户端
        client = CuratorFrameworkFactory.builder().connectString(resourceConfig.getZookeeperServer())
                .sessionTimeoutMs(10000).retryPolicy(retryPolicy).namespace("admin").build();
        // 启动客户端
        client.start();

        client = client.usingNamespace("admin");

        try {
            // 判断在admin命名空间下是否有bgm节点  /admin/bmg
            if (client.checkExists().forPath("/bgm") == null) {
                /**
                 * 对于zk来讲，有两种类型的节点:
                 * 持久节点: 当你创建一个节点的时候，这个节点就永远存在，除非你手动删除
                 * 临时节点: 你创建一个节点之后，会话断开，会自动删除，当然也可以手动删除
                 */
                client.create().creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT)		// 节点类型：持久节点
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)			// acl：匿名权限
                        .forPath("/bgm");
                log.info("zookeeper初始化成功...");

                log.info("zookeeper服务器状态：{}", client.isStarted());
            }
        } catch (Exception e) {
            log.error("zookeeper客户端连接、初始化错误...");
            e.printStackTrace();
        }



    }

    /**
     * @Description: 增加或者删除bgm，向zk-server创建子节点，供小程序后端监听
     */
    public void sendBgmOperator(String bgmId, String operObj) {
        try {

            client.create().creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)		// 节点类型：持久节点
                    .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)			// acl：匿名权限
                    .forPath("/bgm/" + bgmId, operObj.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
