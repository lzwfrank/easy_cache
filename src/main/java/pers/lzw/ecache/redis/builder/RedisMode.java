package pers.lzw.ecache.redis.builder;

import pers.lzw.ecache.redis.source.RedisSource;
import pers.lzw.ecache.redis.source.jedis.impl.SentinelJedisSource;
import pers.lzw.ecache.redis.source.jedis.impl.SingleJedisSource;

/**
 * @description: redis连接模式
 * @author: liu.zhengwei
 * @create: 2020/12/18 11:16
 */
public enum RedisMode {

    single(SingleJedisSource.class), sentinel(SentinelJedisSource.class);

    private Class<? extends RedisSource> resource;

    RedisMode(Class<? extends RedisSource> resource) {
        this.resource = resource;
    }

    public Class<? extends RedisSource> getResource() {
        return this.resource;
    }


}
