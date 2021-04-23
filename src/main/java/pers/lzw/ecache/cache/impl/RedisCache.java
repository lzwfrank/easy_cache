package pers.lzw.ecache.cache.impl;

import pers.lzw.ecache.cache.AbstractCache;
import pers.lzw.ecache.redis.JedisPoolUtils;
import pers.lzw.ecache.support.CacheValueHolder;
import pers.lzw.ecache.support.ValueConvertor;
import pers.lzw.ecache.support.config.CacheBuilderConfig;

import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;


public class RedisCache extends AbstractCache {

//    private static final String CACHE_HASH_KEY ="cache";
//
//    private static final int CACHE_INDEX = 1;
//
//    //默认过期时间为1小时
//    public static final Integer DEFAULT_EXPIRE = ConfigTemplate.buildDefaultConfigTemplate()
//            .getPropertyAsNumber("cache.exprire",3600, Integer.class);

    private CacheBuilderConfig config;

    public RedisCache(CacheBuilderConfig config) {
        this.config = config;
        checkNotNull(config.getValueConvertor(),"convertor can't be null");
    }

    @Override
    public CacheBuilderConfig getConfig() {
        return config;
    }

    @Override
    public Optional<CacheValueHolder> doGet(String key) {
        CacheValueHolder convertorValue = null;
        byte[] json = JedisPoolUtils.newBuilder().get(key.getBytes(), config.getCacheIndex());
        if (json != null && json.length > 0) {
            convertorValue  = getConfig().getValueConvertor().decode(json);
        }
        return Optional.ofNullable(convertorValue);
    }

    @Override
    public void doPut(String key, Object value, int expireSecond) {
        CacheValueHolder cacheValueHolder = new CacheValueHolder(value, expireSecond);
        Optional<byte[]> convertorValue  = getConfig().getValueConvertor().encode(cacheValueHolder);
        byte[] bytes = convertorValue.isPresent()? convertorValue.get(): ValueConvertor.EMPTY_ARRAY;
        //依托redis单线程模型，暂不考虑多线程情况下多次缓存问题
        JedisPoolUtils.newBuilder().set(key.getBytes(), bytes, expireSecond, config.getCacheIndex());
    }


    @Override
    public void doDeleteByPre(String key) {
        Set<String> keys = JedisPoolUtils.newBuilder().cacheKeys(key, config.getCacheIndex());
        keys.stream().forEach(cacheKey -> JedisPoolUtils.newBuilder().del(cacheKey, config.getCacheIndex()));
    }

}
