package com.zymonster.utils;

import java.io.Serializable;

/**
 * @author zhouyang
 * @date 2019/3/7
 */
public class Message implements Serializable {

    private String username;

    private String mailAddress;


    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
