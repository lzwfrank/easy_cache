package pers.lzw.ecache.redis.builder;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.util.Pool;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Defaults.defaultValue;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @description: 通用的jedis构建管理类
 * @author: liu.zhengwei
 * @create: 2020/12/18 16:27
 */
public abstract class BaseJedisPoolManager implements JedisManager  {

    protected JedisConf jedisConf;

    @Override
    public Pool<Jedis> newPool(JedisConf jedisConf) {
        checkNotNull(jedisConf);
        this.jedisConf = jedisConf;

        JedisPoolConfig config = new JedisPoolConfig();
        //最大连接数, 默认100个
        config.setMaxTotal(jedisConf.getMaxActive().get());
        //最大空闲连接数, 默认100个
        config.setMaxIdle(jedisConf.getMaxIdle().get());
        //最小空闲连接数, 默认100个
        config.setMinIdle(jedisConf.getMinIdle().get());
        //获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
        config.setMaxWaitMillis(jedisConf.getMaxWait().get());
        //在获取连接的时候检查有效性, 是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
        config.setTestOnBorrow(true);
        //在空闲时检查有效性
        config.setTestWhileIdle(true);
        String reidsNodes = jedisConf.getNodes().orElse(defaultValue(String.class));
        String password = jedisConf.getPwd().orElse(defaultValue(String.class));
        checkNotNull(reidsNodes,"spring.redis.nodes can't be null");
        //sentinel地址集合
        List<String> nodes = reidsNodes.contains(",")?
                Arrays.asList(reidsNodes.split(",")):
                Arrays.asList(reidsNodes.split(";"));
        Set<String> set= new HashSet<>(nodes);

        return doBuild(set, password, config);
    }

    public abstract Pool<Jedis> doBuild(Set<String> nodes, String password, JedisPoolConfig config);
}
