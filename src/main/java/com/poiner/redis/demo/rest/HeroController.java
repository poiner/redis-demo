package com.poiner.redis.demo.rest;

import com.poiner.redis.demo.module.HeroDTO;
import com.poiner.redis.demo.module.HeroEditInfoDTO;
import com.poiner.redis.demo.service.HeroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/heroes")
@Validated
public class HeroController {
    private final HeroService heroService;

    @Autowired
    public HeroController(HeroService heroService) {
        this.heroService = heroService;
    }

    /**
     * 根据ID查找英雄详细信息
     * @param id 英雄ID
     * @return 英雄详细信息
     */
    @GetMapping(value = "/{id}")
    public HeroDTO findById(
            @PathVariable(value = "id") String id
    ) {
        return heroService.findById(id);
    }

    /**
     * 查询全部英雄
     * @param name 英雄名称，支持模糊查询
     * @return 英雄列表
     */
    @GetMapping(value = "/all")
    public List<HeroDTO> findAll(
            @RequestParam(value = "name", required = false) String name
    ) {
        return heroService.findAll(name);
    }

    /**
     * 创建英雄
     * @param editInfo 英雄编辑信息
     * @return 英雄详细信息
     */
    @PostMapping(value = "")
    public HeroDTO create(
            @RequestBody @Validated HeroEditInfoDTO editInfo
    ) {
        return heroService.create(editInfo);
    }

    /**
     * 修改英雄
     * @param id 英雄ID
     * @param editInfo 英雄编辑信息
     * @return 英雄详细信息
     */
    @PutMapping(value = "/{id}")
    public HeroDTO update(
            @PathVariable(value = "id") String id,
            @RequestBody @Validated HeroEditInfoDTO editInfo
    ) {
        return heroService.update(id, editInfo);
    }

    /**
     * 删除英雄
     * @param id 英雄ID
     */
    @DeleteMapping(value = "/{id}")
    public void delete(
            @PathVariable(value = "id") String id
    ) {
        heroService.delete(id);
    }
}
