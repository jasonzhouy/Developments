package com.zymonster.cofig;


import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.Queue;

/**
 * @author zhouyang
 * @date 2019/3/7
 */

@Configuration
public class ActiveMQConfig {

    @Bean
    public Queue queue(){
        return new ActiveMQQueue("mail-test");
    }
}
