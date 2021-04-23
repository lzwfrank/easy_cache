package pers.lzw.ecache.support.config.impl;

import pers.lzw.ecache.cache.BaseCache;
import pers.lzw.ecache.cache.impl.RedisCache;
import pers.lzw.ecache.builder.source.ICacheHandlerSource;
import pers.lzw.ecache.support.config.AbstractCacheBuilderConfig;

public class CommonCacheConfig extends AbstractCacheBuilderConfig<CommonCacheConfig> {

    private BaseCache baseCache;

    public CommonCacheConfig(ICacheHandlerSource cacheHandler) {
        super(cacheHandler);
    }

    @Override
    public CommonCacheConfig self() {
        return this;
    }

    public <T> CommonCacheConfig set(BaseCache<T> baseCache) {
        this.baseCache = baseCache;
        return this;
    }

    @Override
    public <T> BaseCache<T> buildCache() {
        //没有指定cache方式时 默认为RedisCache
        if (baseCache == null) {
            baseCache = new RedisCache(this);
        }
        return baseCache;
    }
}