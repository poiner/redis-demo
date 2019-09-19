package com.poiner.redis.demo.spring.json;

import com.fasterxml.jackson.annotation.JsonFilter;

/**
 * DynamicFilterMixIn
 *
 * @author Du Ping
 * @date 2018-09-28
 */
@JsonFilter(DynamicFilterProvider.FILTER_ID)
public class DynamicFilterMixIn {
}