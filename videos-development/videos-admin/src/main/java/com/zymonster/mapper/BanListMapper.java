package com.zymonster.mapper;

import com.zymonster.domain.Ban.BanPOJO;
import com.zymonster.domain.BanDemo;

import java.util.List;

/**
 * @author zhouyang
 * @date 2019/4/26
 */
public interface BanListMapper {

    void addBanList(BanDemo banDemo);

    List<BanPOJO> getBanList(String username);

    void cancelBanList(String id);
}
