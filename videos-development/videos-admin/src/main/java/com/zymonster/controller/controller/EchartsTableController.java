package com.zymonster.controller.controller;

import com.zymonster.common.utils.ResultJSON;
import com.zymonster.domain.charts.Chart1;
import com.zymonster.domain.charts.Chart1POJO;
import com.zymonster.domain.charts.Chart2;
import com.zymonster.domain.charts.Chart2POJO;
import com.zymonster.service.ChartsService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhouyang
 * @date 2019/4/25
 */

@RestController
@RequestMapping("/charts")
public class EchartsTableController {

    @Autowired
    private ChartsService chartsServiceImpl;

    @GetMapping("/chart1")
    public ResultJSON getDataChart1(){
        List<Chart1> result = chartsServiceImpl.getChart1();
        List<String> name = new ArrayList<>();
        List<Integer> nums = new ArrayList<>();
        for (Chart1 chart1 :result) {
            name.add(chart1.getTagName());
            nums.add(chart1.getNums());
        }
        Chart1POJO chart1POJO = new Chart1POJO();
        chart1POJO.setName(name);
        chart1POJO.setNums(nums);
        return ResultJSON.ok(chart1POJO);
    }

    @GetMapping("/chart2")
    public ResultJSON getDataChart2(){
        List<Chart2> result = chartsServiceImpl.getChart2();
        List<String> name = new ArrayList<>();
        for (Chart2 chart2 :result) {
            name.add(chart2.getName());
        }
        Chart2POJO chart2POJO = new Chart2POJO();
        chart2POJO.setResult(result);
        chart2POJO.setName(name);

        return ResultJSON.ok(chart2POJO);
    }
}
