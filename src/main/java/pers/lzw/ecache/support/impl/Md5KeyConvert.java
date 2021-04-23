package pers.lzw.ecache.support.impl;

import pers.lzw.ecache.support.CacheKeyConverter;
import pers.lzw.ecache.util.StringUtils;

/**
 * 转换成md5
 * *@author:wen.wen
 * *@create 2020-08-24 16:05
 **/
public class Md5KeyConvert implements CacheKeyConverter {

    @Override
    public String convert(String cacheKey) {
        return StringUtils.convertToMD5(cacheKey);
    }
}
