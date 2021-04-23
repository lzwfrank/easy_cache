package pers.lzw.ecache.support.config.impl;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import pers.lzw.ecache.builder.source.ICacheHandlerSource;
import pers.lzw.ecache.cache.BaseCache;
import pers.lzw.ecache.cache.impl.MultiLevelCache;
import pers.lzw.ecache.support.ValueConvertor;
import pers.lzw.ecache.support.config.AbstractCacheBuilderConfig;

import java.util.LinkedList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @description:
 * @author: liu.zhengwei
 * @create: 2020/12/10 13:44
 */
@Slf4j
public class MultilevelCacheConfig extends AbstractCacheBuilderConfig<MultilevelCacheConfig> {

    @Getter
    private List<BaseCache> caches = new LinkedList<>();

    public MultilevelCacheConfig(ICacheHandlerSource cacheHandler) {
        super(cacheHandler);
    }

    public MultilevelCacheConfig setCaches(BaseCache... baseCaches) {
        for (int i = 0; i < baseCaches.length; i++) {
            if (baseCaches[i] !=  null) {
                caches.add(baseCaches[i]);
            }else {
                log.error("第{}baseCache为null,请检查",i);
            }
        }
        return this;
    }

    @Override
    public <T> MultiLevelCache<T> buildCache() {
        checkArgument(!caches.isEmpty(), "caches can't be null");
        MultiLevelCache<T> multiLevelCache = new MultiLevelCache(this);
        return multiLevelCache;
    }

    @Override
    public MultilevelCacheConfig self() {
        return this;
    }

    @Override
    public MultilevelCacheConfig setValueConvertor(ValueConvertor valueConvertor) {
        log.warn("MultilevelCacheConfig not support this method");
        return self();
    }

    @Override
    public MultilevelCacheConfig setExpireSecond(int expireSecond) {
        log.warn("MultilevelCacheConfig not support this method");
        return self();
    }

    @Override
    public MultilevelCacheConfig setCacheIndex(int cacheIndex) {
        log.warn("MultilevelCacheConfig not support this method");
        return self();
    }
}
