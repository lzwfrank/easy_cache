package pers.lzw.ecache.support;

/**
 * 缓存key键生成策略
 * *@author:wen.wen
 * *@create 2020-08-24 15:55
 **/
public interface CacheKeyConverter {

    String convert(String cacheKey);
}
