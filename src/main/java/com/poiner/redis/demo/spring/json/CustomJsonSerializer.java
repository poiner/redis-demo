package com.poiner.redis.demo.spring.json;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 自定义JSON序列化类
 *
 * @author Du Ping
 * @date 2018-09-16
 */
public class CustomJsonSerializer {

    private Map<Class<?>, Set<String>> includeMap = new HashMap<>();
    private Map<Class<?>, Set<String>> filterMap = new HashMap<>();

    private void include(Class<?> type, String[] fields) {
        addToMap(includeMap, type, fields);
    }

    private void filter(Class<?> type, String[] fields) {
        addToMap(filterMap, type, fields);
    }

    private void addToMap(Map<Class<?>, Set<String>> map, Class<?> type, String[] fields) {
        Set<String> fieldSet = map.getOrDefault(type, new HashSet<>());
        fieldSet.addAll(Arrays.asList(fields));
        map.put(type, fieldSet);
    }

    /**
     * @param clazz   target type
     * @param include include fields
     * @param filter  filter fields
     */
    private void filter(Class<?> clazz, String include, String filter) {
        if (clazz == null) {
            return;
        }
        if (StringUtils.isNotBlank(include)) {
            this.include(clazz, include.split(","));
        }
        if (StringUtils.isNotBlank(filter)) {
            this.filter(clazz, filter.split(","));
        }
    }

    private void filter(Json json) {
        this.filter(json.type(), json.include(), json.filter());
    }

    boolean apply(Class<?> type, String name) {
        Set<String> includeFields = includeMap.get(type);
        Set<String> filterFields = filterMap.get(type);

        if (includeFields != null && includeFields.contains(name)) {
            return true;
        }

        if (filterFields != null && !filterFields.contains(name)) {
            return true;
        }
        return (includeFields == null && filterFields == null);

    }

    public void setFilters(MethodSignature methodSignature) {
        Method method = methodSignature.getMethod();
        Annotation[] annotations = method.getDeclaredAnnotations();

        Arrays.asList(annotations).forEach(a -> {
            if (a instanceof Json) {
                Json json = (Json) a;
                this.filter(json);
            } else if (a instanceof Jsons) {
                Jsons jsons = (Jsons) a;
                Arrays.asList(jsons.value()).forEach(this::filter);
            }
        });
    }
}
