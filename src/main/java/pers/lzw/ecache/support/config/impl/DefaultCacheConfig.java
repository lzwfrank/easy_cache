package pers.lzw.ecache.support.config.impl;

import pers.lzw.ecache.cache.BaseCache;
import pers.lzw.ecache.builder.CacheHandlerBuilder;
import pers.lzw.ecache.exception.CacheException;
import pers.lzw.ecache.support.config.CacheBuilderConfig;

/**
 * @description:
 * @author: liu.zhengwei
 * @create: 2020/12/10 13:37
 */
public class DefaultCacheConfig extends CacheBuilderConfig<DefaultCacheConfig> {

    @Override
    public DefaultCacheConfig self() {
        return this;
    }

    @Override
    public <T> BaseCache<T> buildCache() {
        throw new CacheException("not support this method");
    }

    @Override
    public CacheHandlerBuilder complete() {
        throw new CacheException("not support this method");
    }
}
