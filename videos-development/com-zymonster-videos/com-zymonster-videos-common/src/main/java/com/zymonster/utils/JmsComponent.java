package com.zymonster.utils;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.jms.Queue;
import javax.mail.MessagingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhouyang
 * @date 2019/3/7
 */

@Component
public class JmsComponent {

    @Autowired
    private  JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    private Queue queue;

    @Autowired
    private MailServer mailServer;


    public void send(Message message) {

        jmsMessagingTemplate.convertAndSend(this.queue,message);

    }

    @JmsListener(destination = "mail-test")
    public void receive(Message message)throws MessagingException {

        System.out.println(message.getMailAddress());
        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put("to", new String[]{message.getMailAddress()});
        valueMap.put("title", "欢迎注册！");
        valueMap.put("username", message.getUsername());
        valueMap.put("time", new Date());

        mailServer.sendSimpleMail(valueMap);

    }




}
