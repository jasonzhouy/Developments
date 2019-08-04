package com.zymonster.domain.charts;

import java.util.List;

/**
 * @author zhouyang
 * @date 2019/4/25
 */
public class Chart2POJO {

    private List<Chart2> result;

    private List<String> name;

    public List<Chart2> getResult() {
        return result;
    }

    public void setResult(List<Chart2> result) {
        this.result = result;
    }

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }
}
