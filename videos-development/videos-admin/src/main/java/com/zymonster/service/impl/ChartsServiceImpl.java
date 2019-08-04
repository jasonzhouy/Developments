package com.zymonster.service.impl;

import com.zymonster.domain.charts.Chart1;
import com.zymonster.domain.charts.Chart2;
import com.zymonster.mapper.ChartsMapper;
import com.zymonster.service.ChartsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zhouyang
 * @date 2019/4/25
 */

@Service
public class ChartsServiceImpl implements ChartsService {

    @Autowired
    private ChartsMapper chartsMapper;

    @Override
    public List<Chart1> getChart1(){
        List<Chart1> result = chartsMapper.getChart1();
        return result;
    }

    @Override
    public List<Chart2> getChart2(){
        List<Chart2> result = chartsMapper.getChart2();
        return result;
    }
}
