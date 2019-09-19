package com.poiner.redis.demo.mapper;

import com.poiner.redis.demo.entity.Hero;
import com.poiner.redis.demo.module.HeroDTO;
import com.poiner.redis.demo.module.HeroEditInfoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface HeroMapper {
    /**
     * 编辑信息转实体
     * @param editInfo 编辑信息
     * @return 实体
     */
    Hero toEntity(HeroEditInfoDTO editInfo);

    /**
     * 更新实体
     * @param entity 实体
     * @param editInfo 编辑信息
     */
    void updateEntity(@MappingTarget Hero entity, HeroEditInfoDTO editInfo);

    /**
     * 实体转DTO
     * @param entity 实体
     * @return DTO
     */
    HeroDTO toDTO(Hero entity);

    /**
     * 实体集合转DTO列表
     * @param entities 实体集合
     * @return DTO列表
     */
    List<HeroDTO> toList(Collection<Hero> entities);
}
