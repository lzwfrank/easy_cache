package pers.lzw.ecache.redis.builder.impl;

import lombok.NoArgsConstructor;
import pers.lzw.ecache.redis.builder.BaseJedisPoolManager;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.util.Pool;

import java.util.Set;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * @description:
 * @author: liu.zhengwei
 * @create: 2019/04/10 11:13
 */
@NoArgsConstructor
public class JedisSentinelManager extends BaseJedisPoolManager {

//    @Override
//    public Pool<Jedis> newPool(JedisConf jedisConf) {
//        JedisPoolConfig config = new JedisPoolConfig();
//        Integer MaxWaitMillis = configTemplate.getPropertyAsNumber("redis.maxWait", 10000,Integer.class);
//        //最大连接数, 默认100个
//        config.setMaxTotal(configTemplate.getPropertyAsNumber("redis.maxActive", 100, Integer.class));
//        //最大空闲连接数, 默认100个
//        config.setMaxIdle(configTemplate.getPropertyAsNumber("redis.maxIdle", 100, Integer.class));
//        //最小空闲连接数, 默认100个
//        config.setMinIdle(configTemplate.getPropertyAsNumber("redis.minIdle",0, Integer.class));
//        //获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
//        config.setMaxWaitMillis(MaxWaitMillis);
//        //在获取连接的时候检查有效性, 是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
//        config.setTestOnBorrow(true);
//        //在空闲时检查有效性
//        config.setTestWhileIdle(true);
//        String reidsNodes = jedisConf.getNodes().orElse(defaultValue(String.class));
//        String password = jedisConf.getPwd().orElse(defaultValue(String.class));
//        checkNotNull(reidsNodes,"spring.redis.nodes can't be null");
//        //sentinel地址集合
//        List<String> nodes = reidsNodes.contains(",")?
//                Arrays.asList(reidsNodes.split(",")):
//                Arrays.asList(reidsNodes.split(";"));
//        Set<String> set= new HashSet<>(nodes);
//        Pool<Jedis> jedisPool = isNullOrEmpty(password)?
//                new JedisSentinelPool(jedisConf.getMasterName().get(),set,config):
//                new JedisSentinelPool(jedisConf.getMasterName().get(),set,config,password);
//        return jedisPool;
//    }

    @Override
    public Pool<Jedis> doBuild(Set<String> nodes, String password, JedisPoolConfig config) {
        return isNullOrEmpty(password)?
                new JedisSentinelPool(jedisConf.getMasterName().get(),nodes,config):
                new JedisSentinelPool(jedisConf.getMasterName().get(),nodes,config,password);
    }
}
