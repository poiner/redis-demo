package com.poiner.redis.demo.service;

import com.github.wenhao.jpa.Specifications;
import com.poiner.redis.demo.aspect.RedisLockAnnoation;
import com.poiner.redis.demo.entity.Hero;
import com.poiner.redis.demo.exception.NoSuchDataException;
import com.poiner.redis.demo.mapper.HeroMapper;
import com.poiner.redis.demo.module.HeroDTO;
import com.poiner.redis.demo.module.HeroEditInfoDTO;
import com.poiner.redis.demo.repository.HeroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service("heroService")
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class HeroServiceImpl implements HeroService {

    private final HeroRepository heroRepository;

    private final HeroMapper heroMapper;

    @Autowired
    public HeroServiceImpl(HeroRepository heroRepository, HeroMapper heroMapper) {
        this.heroRepository = heroRepository;
        this.heroMapper = heroMapper;
    }

    @Override
    @Cacheable(cacheNames = {"hero"}, key = "#id + '_hero'", unless = "#result eq null")
    public HeroDTO findById(String id) {
        Optional<Hero> heroOptional = heroRepository.findById(Long.valueOf(id));
        Hero hero = heroOptional.orElseThrow(() -> new NoSuchDataException(id));
        return heroMapper.toDTO(hero);
    }

    @Override
    @Cacheable(cacheNames = {"heroes"}, key = "'heroes' + #root.methodName + '_' + T(java.util.Objects).hash(#name)", unless = "#result.size() eq 0")
    public List<HeroDTO> findAll(String name) {
        List<Hero> heroes = heroRepository.findAll(Specifications.<Hero>and()
                .like(name != null && !name.isEmpty(), "name", "%" + name + "%").build());
        return heroMapper.toList(heroes);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    @Caching(put = @CachePut(cacheNames = "hero", key = "#result.id + '_hero'", unless = "#result eq null "), evict = {@CacheEvict(cacheNames = {"heroes"}, allEntries = true)})
    public HeroDTO create(HeroEditInfoDTO editInfo) {
        Hero hero = heroMapper.toEntity(editInfo);
        hero = heroRepository.save(hero);
        return heroMapper.toDTO(hero);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    @Caching(put = @CachePut(cacheNames = "hero", key = "#result.id + '_hero'", unless = "#result eq null "), evict = {@CacheEvict(cacheNames = {"heroes"}, allEntries = true)})
    @RedisLockAnnoation(keyPrefix = "method_hero_", keys = {"update"})
    public HeroDTO update(String id, HeroEditInfoDTO editInfo) {
        Optional<Hero> heroOptional = heroRepository.findById(Long.valueOf(id));
        Hero hero = heroOptional.orElseThrow(() -> new NoSuchDataException(id));
        heroMapper.updateEntity(hero, editInfo);
        hero = heroRepository.save(hero);
        return heroMapper.toDTO(hero);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    @CacheEvict(cacheNames = {"hero", "heroes"}, allEntries = true)
    public void delete(String id) {
        Optional<Hero> heroOptional = heroRepository.findById(Long.valueOf(id));
        Hero hero = heroOptional.orElseThrow(() -> new NoSuchDataException(id));
        heroRepository.delete(hero);
    }
}
