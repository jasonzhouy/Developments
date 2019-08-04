package com.zymonster.controller.config;

import com.zymonster.controller.ZKCurator;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author zhouyang
 * @date 2019/4/19
 */
@Configuration
@ConfigurationProperties(prefix="com.zymonster")
@PropertySource("classpath:resource.properties")
public class ResourceConfig {

    private String zookeeperServer;
    private String bgmServer;
    private String fileSpace;

    public String getZookeeperServer() {
        return zookeeperServer;
    }
    public void setZookeeperServer(String zookeeperServer) {
        this.zookeeperServer = zookeeperServer;
    }
    public String getBgmServer() {
        return bgmServer;
    }
    public void setBgmServer(String bgmServer) {
        this.bgmServer = bgmServer;
    }
    public String getFileSpace() {
        return fileSpace;
    }
    public void setFileSpace(String fileSpace) {
        this.fileSpace = fileSpace;
    }

    @Bean(initMethod = "init")
    public ZKCurator zkCuratorClient() {
        return new ZKCurator();
    }


}
