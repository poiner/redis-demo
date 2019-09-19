package com.poiner.redis.demo.repository;

import com.poiner.redis.demo.entity.Hero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface HeroRepository extends JpaRepository<Hero, Long>, JpaSpecificationExecutor<Hero> {

}
