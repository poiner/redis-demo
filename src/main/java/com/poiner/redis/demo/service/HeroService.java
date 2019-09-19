package com.poiner.redis.demo.service;

import com.poiner.redis.demo.module.HeroDTO;
import com.poiner.redis.demo.module.HeroEditInfoDTO;

import java.util.List;

public interface HeroService {
    /**
     * 根据ID查找英雄详细信息
     * @param id 英雄ID
     * @return 英雄详细信息
     */
    HeroDTO findById(String id);

    /**
     * 查询全部英雄
     * @param name 英雄名称，支持模糊查询
     * @return 英雄列表
     */
    List<HeroDTO> findAll(String name);

    /**
     * 创建英雄
     * @param editInfo 英雄编辑信息
     * @return 英雄详细信息
     */
    HeroDTO create(HeroEditInfoDTO editInfo);

    /**
     * 修改英雄
     * @param id 英雄ID
     * @param editInfo 英雄编辑信息
     * @return 英雄详细信息
     */
    HeroDTO update(String id, HeroEditInfoDTO editInfo);

    /**
     * 删除英雄
     * @param id 英雄ID
     */
    void delete(String id);
}
