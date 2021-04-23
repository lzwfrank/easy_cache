package pers.lzw.ecache.cache;

import pers.lzw.ecache.support.config.CacheBuilderConfig;

import java.util.Optional;

public interface BaseCache<T> {

    /**
     * 通过key获取缓存结果
     * @param key
     * @return
     */
    Optional<T> getValue(String key);

    /**
     * 保存或更新缓存
     * @param key
     * @param value
     */
    void put(String key, T value);

    /**
     * 删除以key开头的key键
     * @param key
     */
    void deleteByPre(String key);

    /**
     * @description: 获取缓存配置
     * @Author: liuzhengwei
     * @Date: 2020-11-24 14:04
     * @Param: []
     * @Return: com.trs.tcache.support.CacheConfig
     */
    CacheBuilderConfig getConfig();

}
