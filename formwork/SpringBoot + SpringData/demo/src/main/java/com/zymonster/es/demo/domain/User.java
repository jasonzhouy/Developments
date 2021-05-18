package com.zymonster.es.demo.domain;/**
 * @author zhouyang
 * @date 2019/9/1
 */

import javax.persistence.*;

/**
 * @Author: zhouyang
 * @Date: 2019/9/1 13:22
 * @Description:
 **/
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String username;

    private String nickname;

    @Column(name = "fans_counts")
    private Long fansCounts;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Long getFansCounts() {
        return fansCounts;
    }

    public void setFansCounts(Long fansCounts) {
        this.fansCounts = fansCounts;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", fansCounts=" + fansCounts +
                '}';
    }
}
