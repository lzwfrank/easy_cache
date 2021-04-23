package pers.lzw.ecache.cache.impl;

import pers.lzw.ecache.cache.BaseCache;
import pers.lzw.ecache.support.config.CacheBuilderConfig;
import pers.lzw.ecache.support.config.impl.MultilevelCacheConfig;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @description:多级缓存
 * @author: liu.zhengwei
 * @create: 2020/11/24 10:40
 */
public class MultiLevelCache<T> implements BaseCache<T> {

    private MultilevelCacheConfig cacheConfig;

    public MultiLevelCache(MultilevelCacheConfig cacheConfig) {
        checkNotNull(cacheConfig);
        checkArgument(!cacheConfig.getCaches().isEmpty(), "caches can't be null");
        this.cacheConfig = cacheConfig;
    }

    @Override
    public Optional<T> getValue(String key) {
        List<BaseCache> caches = cacheConfig.getCaches();
        for (BaseCache cache : caches) {
            Optional<T> cacheValue = cache.getValue(key);
            if (cacheValue.isPresent()) {
                return cacheValue;
            }
        }
        return Optional.empty();
    }

    @Override
    public void put(String key, Object value) {
        List<BaseCache> caches = cacheConfig.getCaches();
        for (BaseCache cache : caches) {
            //考虑顺序固定，只要有一个put成功 get就能成功。且出现put顺序get不到的情况也可以忽略。(减少锁效率的影响)
            cache.put(key, value);
        }
    }

    @Override
    public void deleteByPre(String key) {
        List<BaseCache> caches = cacheConfig.getCaches();
        for (BaseCache cache : caches) {
            cache.deleteByPre(key);
        }
    }

    @Override
    public CacheBuilderConfig getConfig() {
        return cacheConfig;
    }

//    @Override
//    public CacheBuilderConfig getConfig() {
//        //MultiLevelCache本身不进行配置管理。默认使用第一个cache的配置
//        return cacheConfig;
//    }
//
//    @Override
//    public Optional<CacheValueHolder<T>> doGet(String key) {
//        List<BaseCache> caches = cacheConfig.getCaches();
//        for (BaseCache cache : caches) {
//            Optional<T> CacheValueHolder = cache.getValue(key);
//            if (CacheValueHolder.isPresent()) {
//                return CacheValueHolder;
//            }
//        }
//        return Optional.empty();
//    }
//
//    @Override
//    public void doPut(String key, T value, int expireSecond) {
//        List<BaseCache> caches = cacheConfig.getCaches();
//        for (BaseCache cache : caches) {
//            //考虑顺序固定，只要有一个put成功 get就能成功。且出现put顺序get不到的情况也可以忽略。(减少锁效率的影响)
//            cache.put(key, value);
//        }
//    }
//
//
//    @Override
//    public void doDelete(String key) {
//        List<BaseCache> caches = cacheConfig.getCaches();
//        for (BaseCache cache : caches) {
//            cache.delete(key);
//        }
//    }
}
