package com.zymonster.postgrey.domain.DO;/**
 * @author zhouyang
 * @date 2019/9/5
 */

/**
 * @Author: zhouyang
 * @Date: 2019/9/5 22:00
 * @Description:
 **/
public class User {

    private String id;

    private String name;

    private Integer age;

    private Integer sex;

    private String description;

    public User(String id, String name, Integer age, Integer sex, String description) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
