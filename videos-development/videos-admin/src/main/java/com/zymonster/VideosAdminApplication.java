package com.zymonster;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.zymonster.mapper")
@ComponentScan(basePackages = {"com.zymonster","org.n3r.idworker"})
public class VideosAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(VideosAdminApplication.class, args);
    }

}
