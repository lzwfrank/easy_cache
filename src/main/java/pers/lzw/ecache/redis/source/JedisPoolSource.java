package pers.lzw.ecache.redis.source;

import pers.lzw.ecache.redis.builder.JedisConf;
import pers.lzw.ecache.redis.builder.RedisClientBuilder;
import pers.lzw.ecache.redis.source.jedis.AbstractJedisSource;
import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @description:使用jedisPool的连接管理
 * @author: liu.zhengwei
 * @create: 2020/12/18 10:54
 */
public abstract class JedisPoolSource extends AbstractJedisSource {

    //用于存储多个redis连接
    private static Map<String, Pool<Jedis>> jedisPoolMap = new ConcurrentHashMap<>();

    private String key = null;//标注redis连接

    public JedisPoolSource(JedisConf jedisConf) {
        checkNotNull(jedisConf, "jedisConf can't be null");
        key = jedisConf.getUniqueId();
        if (!jedisPoolMap.containsKey(key)) {
            synchronized (JedisPoolSource.class) {
                if (!jedisPoolMap.containsKey(key)) {
                    Pool<Jedis> jedisPool = RedisClientBuilder.jedisPoolBuild(jedisConf);
                    jedisPoolMap.put(key, jedisPool);
                }
            }
        }
    }


    /**
     * 获取jedis连接实例
     *
     * @return
     */
    @Override
    public Jedis getResource() {
        return jedisPoolMap.get(key).getResource();
    }

}
