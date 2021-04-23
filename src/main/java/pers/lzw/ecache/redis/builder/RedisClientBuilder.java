package pers.lzw.ecache.redis.builder;


import pers.lzw.ecache.redis.builder.impl.JedisPoolManager;
import pers.lzw.ecache.redis.builder.impl.JedisSentinelManager;
import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;

/**
 * @description:redis相关连接生成者
 * @author: liu.zhengwei
 * @create: 2019/05/21 18:05
 */
public class RedisClientBuilder {

    /**
     * @description: 通过传入jedisConf的方式构建redis连接池
     * @Author: liuzhengwei
     * @Date: 2020-10-10 10:33
     * @Param: [jedisConf]
     * @Return: redis.clients.util.Pool<redis.clients.jedis.Jedis>
     */
    public static Pool<Jedis> jedisPoolBuild(JedisConf jedisConf) {
        JedisManager jedisManager = jedisConf.isSentinel()?new JedisSentinelManager(): new JedisPoolManager();
        return jedisManager.newPool(jedisConf);
    }

    /**
     * @description: 使用默认构建的方式进行redis连接池创建
     * @Author: liuzhengwei
     * @Date: 2020-10-10 10:34
     * @Param: []
     * @Return: redis.clients.util.Pool<redis.clients.jedis.Jedis>
     */
    public static Pool<Jedis> jedisPoolBuild() {
        //判断是否有配置spring.redis.master,若其值不为空，则认为是哨兵模式
        JedisConf jedisConf = JedisConf.init();

        return jedisPoolBuild(jedisConf);
    }
}
