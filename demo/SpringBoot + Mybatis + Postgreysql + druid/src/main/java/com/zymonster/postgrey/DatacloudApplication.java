package com.zymonster.postgrey;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.sinovatio.deep.datacloud.mapper")
public class DatacloudApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatacloudApplication.class, args);
    }

}
