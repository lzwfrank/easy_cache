package pers.lzw.ecache.support.impl;

import pers.lzw.ecache.support.CacheKeyConverter;

/**
 * 构造一个空的转换
 * *@author:wen.wen
 * *@create 2020-08-24 16:12
 **/
public class EmptyKeyConvert implements CacheKeyConverter {

    @Override
    public String convert(String cacheKey) {
        return cacheKey;
    }
}
