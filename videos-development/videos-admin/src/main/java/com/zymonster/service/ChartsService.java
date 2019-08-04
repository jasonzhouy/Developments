package com.zymonster.service;

import com.zymonster.domain.charts.Chart1;
import com.zymonster.domain.charts.Chart2;

import javax.validation.constraints.Null;
import java.util.List;

/**
 * @author zhouyang
 * @date 2019/4/25
 */
public interface ChartsService {

    List<Chart1> getChart1();

    List<Chart2> getChart2();

}
