package pers.lzw.ecache.cache.impl;

import pers.lzw.ecache.cache.AbstractCache;
import pers.lzw.ecache.support.config.CacheBuilderConfig;
import pers.lzw.ecache.support.CacheValueHolder;
import org.apache.commons.collections4.map.LRUMap;

import java.util.Optional;


public class LocalCache extends AbstractCache {

    //TODO 后续基于copyOnWrite算法重写LRUMap
    private static LRUMap<String, Object> lruMap;

    private CacheBuilderConfig cacheConfig;

    /**
     * 由于单个程序理论上只允许有一个localCache,所以方法只会实例化一次
     */
    public LocalCache(CacheBuilderConfig cacheConfig){
        this.cacheConfig = cacheConfig;
        if (lruMap == null ) {
            synchronized (LocalCache.class) {
                if (lruMap == null) {
                    lruMap = new LRUMap(100);
                }
            }
        }
    }

    @Override
    public CacheBuilderConfig getConfig() {
        return cacheConfig;
    }

    @Override
    public Optional<CacheValueHolder> doGet(String key) {
        CacheValueHolder valueHolder = (CacheValueHolder) lruMap.get(key);
        if (valueHolder != null) {
            long expireTime = valueHolder.getExpireTime();
            if (System.currentTimeMillis() > expireTime && expireTime > 0) {
                lruMap.remove(key);
                valueHolder = null;
            }
        }
        return Optional.ofNullable(valueHolder);
    }

    @Override
    public void doPut(String key, Object value, int expireSecond) {
        CacheValueHolder cacheValueHolder = new CacheValueHolder(value, expireSecond);
        lruMap.put(key,cacheValueHolder);
    }

    @Override
    public void doDeleteByPre(String key) {
        //考虑clear比遍历删除性能好一些
        lruMap.clear();
    }


}
