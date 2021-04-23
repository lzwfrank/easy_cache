package pers.lzw.ecache.redis.source.jedis.impl;

import pers.lzw.ecache.redis.builder.JedisConf;
import pers.lzw.ecache.redis.source.JedisPoolSource;

/**
 * @description: 哨兵模式下的jedis
 * @author: liu.zhengwei
 * @create: 2020/12/18 10:35
 */
public class SentinelJedisSource extends JedisPoolSource {

    public SentinelJedisSource(JedisConf jedisConf) {
        super(jedisConf);
    }

}
