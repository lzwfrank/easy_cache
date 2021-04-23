package pers.lzw.ecache.redis.source.jedis;

import com.google.common.base.Defaults;
import pers.lzw.ecache.redis.source.RedisSource;
import redis.clients.jedis.Jedis;

import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @description: 通用jedis处理
 * @author: liu.zhengwei
 * @create: 2020/12/18 10:29
 */
public abstract class AbstractJedisSource implements RedisSource {


    /**
     * 向redis中存入值
     *
     * @param key
     * @param value
     * @param expireSecond 过期时间，当为-1（小于0）时 表示不设置过期时间
     * @param index        redis库
     */
    @Override
    public void set(String key, String value, int expireSecond, int index) {
        checkNotNull(key, "the key must not be null");
        checkNotNull(value, "the value must not be null");
        checkArgument(index >= 0, "index must be greater than or equal to 0");
        try (Jedis jedis = this.getResource();){
            jedis.select(index);
            jedis.set(key, value);
            if (expireSecond >= 0) {
                jedis.expire(key, expireSecond);
            }
        }
    }

    @Override
    public void set(byte[] key, byte[] value, int expireSecond, int index) {
        checkArgument(index >= 0, "index must be greater than or equal to 0");
        checkNotNull(key, "the key must not be null");
        checkNotNull(value, "the value must not be null");
        try (Jedis jedis = this.getResource();){
            jedis.select(index);
            jedis.set(key, value);
            if (expireSecond >= 0)
                jedis.expire(key, expireSecond);
        }
    }

    @Override
    public byte[] get(byte[] key, int index) {
        byte[] value = null;
        checkNotNull(key, "the key must not be null");
        checkArgument(index >= 0, "index must be greater than or equal to 0");
        try (Jedis jedis = this.getResource();){
            jedis.select(index);
            value = jedis.get(key);
        }
        return value;
    }

    /**
     * 获取对应key的对应值
     *
     * @param key
     * @param index redis库
     * @return 当返回为null时表示对应库中无该key
     */
    @Override
    public String get(String key, int index) {
        String value = null;
        checkNotNull(key, "the key must not be null");
        checkArgument(index >= 0, "index must be greater than or equal to 0");
        try (Jedis jedis = this.getResource();){
            jedis.select(index);
            value = jedis.get(key);
        }
        return value;
    }

    @Override
    public String hget(String key, String field, int index) {
        String value = null;
        checkNotNull(key, "the key must not be null");
        checkNotNull(field, "the field must not be null");
        try (Jedis jedis = this.getResource();){
            jedis.select(index);
            value = jedis.hget(key, field);
        }
        return value;
    }

    @Override
    public Map<String, String> hget(String key, int index) {
        Map<String, String> value = null;
        checkNotNull(key, "the key must not be null");
        try (Jedis jedis = this.getResource();){
            jedis.select(index);
            value = jedis.hgetAll(key);
        }
        return value;
    }


    /**
     * 以hash的方式进行存储
     *
     * @param key
     * @param field
     * @param value
     */
    @Override
    public void hset(String key, String field, String value, int index) {
        checkNotNull(key, "the key must not be null");
        checkNotNull(field, "the field must not be null");
        checkNotNull(value, "the value must not be null");
        try (Jedis jedis = this.getResource();){
            jedis.select(index);
            jedis.hset(key, field, value);
        }
    }


    /**
     * 以hash的方式进行存储
     *
     * @param key
     * @param hash
     */
    @Override
    public void hset(String key, Map<String, String> hash, int index) {
        checkNotNull(key, "the key must not be null");
        checkNotNull(hash, "the hash must not be null");
        checkArgument(!hash.isEmpty(), "hash must not be null or empty");
        try (Jedis jedis = this.getResource();){
            jedis.select(index);
            jedis.hmset(key, hash);
        }
    }

    /**
     * 以hash的方式进行存储删除
     *
     * @param key
     * @param index
     * @param fields
     */
    @Override
    public void hdel(String key, int index, String... fields) {
        checkNotNull(key, "the key must not be null");
        checkNotNull(fields, "the field must not be null");
        try (Jedis jedis = this.getResource();){
            jedis.select(index);
            jedis.hdel(key, fields);
        }
    }

    /**
     * 删除redis中的可以键
     *
     * @param key
     * @param index redis库
     */
    @Override
    public void del(String key, int index) {
        checkNotNull(key, "the key must not be null");
        checkArgument(index >= 0, "index must be greater than or equal to 0");
        try (Jedis jedis = this.getResource();){
            jedis.select(index);
            jedis.del(key);
        }
    }

    /**
     * SET if Not eXists. 若key不存在，则创建该key，同时返回true；若key已存在，创建失败，返回false
     * setNx和过期时间已进行原子性操作
     * @param key
     * @param value
     * @param seconds 过期时间，若传入值小于等于0 则默认过期时间为10秒
     * @param index   redis索引库
     * @return
     */
    @Override
    public Boolean setNx(String key, String value, int seconds, int index) {
        String result = Defaults.defaultValue(String.class);
        checkNotNull(key);
        checkNotNull(value);
        try (Jedis jedis = this.getResource();){
            jedis.select(index);
            result= jedis.set(key, value,"nx", "ex", seconds);
        }
        return result != null && "OK".equals(result);
    }


    /**
     * 队列操作。 将一个或多个 value 插入到列表 key 的表头
     * 如果 key 不存在，一个空列表会被创建并执行 push 操作。
     * 当 key 存在但不是列表类型时，返回一个错误。
     *
     * @param key 队列名称
     * @return 队列长度
     */
    @Override
    public Long pushToHead(String key, String... values) {
        Long length = null;
        checkNotNull(key, "the key must not be null");
        try (Jedis jedis = this.getResource();){
            length = jedis.lpush(key, values);
        }
        return length;
    }

    /**
     * 队列操作。 将一个或多个 value 插入到列表 key 的表尾
     * 如果 key 不存在，一个空列表会被创建并执行 push 操作。
     * 当 key 存在但不是列表类型时，返回一个错误。
     *
     * @param key 队列名称
     * @return 队列长度
     */
    @Override
    public Long pushToTail(String key, String... values) {
        Long length = null;
        checkNotNull(key, "the key must not be null");
        try (Jedis jedis = this.getResource();){
            length = jedis.rpush(key, values);
        }
        return length;
    }


    /**
     * 队列操作。 移除并返回列表 key 的头元素。
     * 当key不存在时，返回 null
     *
     * @param key 队列名称
     * @return
     */
    @Override
    public String popFromHead(String key) {
        String value = null;
        checkNotNull(key, "the key must not be null");
        try (Jedis jedis = this.getResource();){
            value = jedis.lpop(key);
        }
        return value;
    }

    /**
     * 队列操作。 移除并返回列表 key 的尾元素。
     * 当key不存在时，返回 null
     *
     * @param key 队列名称
     * @return
     */
    @Override
    public String popFromTail(String key) {
        String value = null;
        checkNotNull(key, "the key must not be null");
        try (Jedis jedis = this.getResource();){
            value = jedis.rpop(key);
        }
        return value;
    }


    /**
     * 获取对应key的对应值.
     *
     * @param key
     * @param index redis库
     * @return 当返回为null时表示对应库中无该key
     */
    @Override
    public Set<String> keys(String key, int index) {
        Set<String> keys = null;
        checkNotNull(key, "the key must not be null");
        checkArgument(index >= 0, "index must be greater than or equal to 0");
        try (Jedis jedis = this.getResource()){
            jedis.select(index);
            keys = jedis.keys(key);
        }
        return keys;
    }
    /**
     * @Description: 判断是否存在key
     * @param key
     * @param index redis库
     * @return 如果redis库中无key,返回false,反之返回true
     * @Date: 2020/8/18
     */
    @Override
    public Boolean exists(String key,int index){
        Boolean exists;
        checkNotNull(key, "the key must not be null");
        checkArgument(index >= 0, "index must be greater than or equal to 0");
        try (Jedis jedis = this.getResource();){
            jedis.select(index);
            exists = jedis.exists(key);
        }
        return exists;
    }

    /**
     * @Description: 判断是否存在以hash方式存储的key,filed
     * @param key
     * @param field
     * @param index redis库
     * @return key或hash表的filed不存在返回false,反之返回true
     * @Date: 2020/8/18
     */
    @Override
    public Boolean hexists(String key,String field,int index){
        Boolean exists;
        checkNotNull(key, "the key must not be null");
        checkNotNull(field, "the field must not be null");
        checkArgument(index >= 0, "index must be greater than or equal to 0");
        try (Jedis jedis = this.getResource();){
            jedis.select(index);
            exists = jedis.hexists(key,field);
        }
        return exists;
    }


    @Override
    public Long getExpire(String key, int index) {
        Long expireSeconds = 0L;
        checkNotNull(key);
        try (Jedis jedis = this.getResource();){
            jedis.select(index);
            expireSeconds = jedis.ttl(key);
        }
        return expireSeconds;
    }

    public abstract Jedis getResource();

}
