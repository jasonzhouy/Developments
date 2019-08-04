package com.zymonster.domain;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author zhouyang
 * @date 2019/3/15
 */

@Table(name = "role")
public class Role {

    @Id
    private Integer id;


    @Column(name = "role")
    private String role;

    @Column(name = "desc")
    private String desc;

    @Column(name = "category")
    private String category;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
