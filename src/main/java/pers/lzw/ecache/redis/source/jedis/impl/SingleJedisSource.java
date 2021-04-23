package pers.lzw.ecache.redis.source.jedis.impl;

import pers.lzw.ecache.redis.builder.JedisConf;
import pers.lzw.ecache.redis.source.JedisPoolSource;

/**
 * @description: 单节点redis
 * @author: liu.zhengwei
 * @create: 2020/12/18 10:50
 */
public class SingleJedisSource extends JedisPoolSource {

    public SingleJedisSource(JedisConf jedisConf) {
        super(jedisConf);
    }

}
