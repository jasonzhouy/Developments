package com.zymonster.mapper;

import com.zymonster.domain.Bgm;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author zhouyang
 * @date 2019/4/8
 */
public interface BgmMapper {

    /*
    func:获取背景音乐列表
     */

   List<Bgm> getBgmList();

   void deleteBgm(@Param("id") String id);

   void addBgm(Bgm bgm);

   Bgm getBgmById(@Param("id") String id);
}
