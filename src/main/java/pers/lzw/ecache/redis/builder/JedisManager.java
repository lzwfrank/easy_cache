package pers.lzw.ecache.redis.builder;

import pers.lzw.ecache.config.ConfigTemplate;
import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;

/**
 * @program jedis链接创建
 * @author: liu.zhengwei
 * @create: 2019/04/10 11:13
 */
public interface JedisManager{

    ConfigTemplate configTemplate = ConfigTemplate.buildDefaultConfigTemplate();

    Pool<Jedis> newPool(JedisConf jedisConf);
}
