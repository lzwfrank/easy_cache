package pers.lzw.ecache.redis;

import com.google.common.base.Defaults;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import pers.lzw.ecache.redis.builder.JedisConf;
import pers.lzw.ecache.redis.source.RedisSource;
import pers.lzw.ecache.redis.source.RedisSourceFactory;
import pers.lzw.ecache.redis.source.jedis.impl.SentinelJedisSource;
import pers.lzw.ecache.redis.source.jedis.impl.SingleJedisSource;
import pers.lzw.ecache.support.CacheValueHolder;
import pers.lzw.ecache.support.ValueConvertor;
import pers.lzw.ecache.support.impl.DefaultValueConvertBuild;

import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JedisPoolUtils {

    private RedisSource redisSource;

    /*
     * Redis中默认设置了16个数据库,编号为0~15，此处默认0号库
     */
    public static final int DEFAULT_INDEX = 0;

    //默认过期时间为10秒
    public static final int DEFAULT_EXPIRE = 10;

    //不设置过期时间
    public static final int NOT_EXPIRE = -1;

    /*
     * 使用setnx创建的key默认放在2号库
     */
    public static final int SETNX_DEFAULT_INDEX = 2;

    private ValueConvertor valueConvertor = DefaultValueConvertBuild.getDefaultValueConvertor();

    /**
     * 使用默认的jedis。连接信息为配置文件配置.
     * 若配置了spring.redis.masterName，则默认进行哨兵模式的加载
     *
     * @return
     */
    public final static JedisPoolUtils newBuilder() {
        JedisPoolUtils jedisPoolUtils = new JedisPoolUtils();
        jedisPoolUtils.redisSource = RedisSourceFactory.newSource();
        return jedisPoolUtils;
    }

    /**
     * 构建无密码的哨兵模式链接
     *
     * @param masterName
     * @param nodes      节点信息，多值用;分割
     * @return
     */
    public final static JedisPoolUtils newSentinel(final String masterName, final String nodes) {
        return newSentinel(masterName, nodes, null);
    }

    /**
     * 有密码的哨兵模式链接
     *
     * @param masterName
     * @param nodes      节点信息，多值用;分割
     * @param pwd        密码
     * @return
     */
    public final static JedisPoolUtils newSentinel(final String masterName, final String nodes, final String pwd) {
        checkNotNull(masterName, nodes);
        JedisConf jedisConf = JedisConf.buildSentinel(masterName, nodes, pwd);
        RedisSource redisSource = new SentinelJedisSource(jedisConf);
        JedisPoolUtils jedisPoolUtils = new JedisPoolUtils();
        jedisPoolUtils.redisSource = redisSource;
        return jedisPoolUtils;
    }

    /**
     * 无密码的单节点链接
     *
     * @param host redis部署ip
     * @param port 端口
     * @return
     */
    public final static JedisPoolUtils newBuilder(final String host, final String port) {
        return newBuilder(host, port, Defaults.defaultValue(String.class));
    }

    /**
     * 有密码的单节点链接
     *
     * @param host
     * @param port
     * @param pwd
     * @return
     */
    public final static JedisPoolUtils newBuilder(final String host, final String port, final String pwd) {
        //构建jedisConf
        JedisConf jedisConf = JedisConf.buildSimple(host, port, pwd);
        //初始化
        RedisSource redisSource = new SingleJedisSource(jedisConf);
        //获取实例
        JedisPoolUtils jedisPoolUtils = new JedisPoolUtils();
        jedisPoolUtils.redisSource = redisSource;
        return jedisPoolUtils;
    }


    public void set(String key, String value) {
        set(key, value, NOT_EXPIRE, DEFAULT_INDEX);
    }

    /**
     * 向redis中存入值
     *
     * @param key
     * @param value
     * @param expireSecond 过期时间，当为-1（小于0）时 表示不设置过期时间
     * @param index        redis库
     */
    public void set(String key, String value, int expireSecond, int index) {
        redisSource.set(key, value, expireSecond, index);
    }

    /**
     * 序列化的方式进行缓存。使用fastJson的方式进行序列化
     * @param key
     * @param value
     * @param expireSecond 过期时间，当为-1（小于0）时 表示不设置过期时间
     * @param <T>
     */
    public <T> void setAsSerialize(String key, T value, int expireSecond) {
        setAsSerialize(key, value, expireSecond, DEFAULT_INDEX);
    }

    /**
     * 序列化的方式进行缓存。使用fastJson的方式进行序列化
     * @param key
     * @param value
     * @param expireSecond 过期时间，当为-1（小于0）时 表示不设置过期时间
     * @param index  redis库
     * @param <T>
     */
    public <T> void setAsSerialize(String key, T value, int expireSecond, int index) {
        CacheValueHolder<T> cacheValueHolder = new CacheValueHolder(value, expireSecond);
        set(key.getBytes(), valueConvertor.encode(cacheValueHolder).get(), expireSecond, index);
    }

    /**
     * 序列化的方式获取对应的结果集。使用fastJson的方式进行序列化
     * @param key
     * @param <T>
     */
    public <T> T getAsSerialize(String key) {
        return this.getAsSerialize(key, DEFAULT_INDEX);
    }

    /**
     * 序列化的方式获取对应的结果集。使用fastJson的方式进行序列化
     * @param key
     * @param index  redis库
     * @param <T>
     */
    public <T> T getAsSerialize(String key, int index) {
        byte[] value = redisSource.get(key.getBytes(), index);
        return (T) valueConvertor.decode(value).getValue();
    }

    /**
     * redis缓存字节数组
     * @param key
     * @param value
     */
    public void set(byte[] key, byte[] value) {
        set(key, value, NOT_EXPIRE, DEFAULT_INDEX);
    }

    public void set(byte[] key, byte[] value, int expireSecond, int index) {
        redisSource.set(key, value, expireSecond, index);
    }

    /**
     * 获取对应key的对应值
     *
     * @param key
     * @return 当返回为null时表示对应库中无该key
     */
    public String get(String key) {
        return get(key,DEFAULT_INDEX);
    }


    /**
     * 根据key字节获取value字节数组
     * @param key
     * @return
     */
    public byte[] get(byte[] key) {
        return get(key, DEFAULT_INDEX);
    }

    public byte[] get(byte[] key, int index) {
        return redisSource.get(key, index);
    }

    /**
     * 获取对应key的对应值
     *
     * @param key
     * @param index redis库
     * @return 当返回为null时表示对应库中无该key
     */
    public String get(String key, int index) {

        return redisSource.get(key, index);
    }

    /**
     * 以hash的方式进行存储
     *
     * @param key
     * @param field
     * @param value
     */
    public void hset(String key, String field, String value) {
        this.hset(key, field, value, DEFAULT_INDEX);
    }

    /**
     * 以hash的方式进行存储
     *
     * @param key
     * @param field
     * @param value
     */
    public void hset(String key, String field, String value, int index) {
        redisSource.hset(key, field, value, index);
    }

    /**
     * 以hash的方式进行存储
     *
     * @param key
     * @param hash
     */
    public void hset(String key, Map<String, String> hash) {
        this.hset(key, hash, DEFAULT_INDEX);
    }

    /**
     * 以hash的方式进行存储
     *
     * @param key
     * @param hash
     */
    public void hset(String key, Map<String, String> hash, int index) {
        redisSource.hset(key, hash, index);
    }

    /**
     * 以hash的方式进行存储
     *
     * @param key
     * @param fields
     */
    public void hdel(String key, String... fields) {
        this.hdel(key, DEFAULT_INDEX, fields);
    }

    /**
     * 以hash的方式进行存储删除
     *
     * @param key
     * @param index
     * @param fields
     */
    public void hdel(String key, int index, String... fields) {
        redisSource.hdel(key, index, fields);
    }

    /**
     * 删除redis中的可以键
     *
     * @param key
     * @param index redis库
     */
    public void del(String key, int index) {
        redisSource.del(key, index);
    }

    /**
     * SET if Not eXists. 若key不存在，则创建该key，同时返回true；若key已存在，创建失败，返回true。
     * 默认过期时间为10秒。setNx和过期时间已进行原子性操作
     *
     * @param key
     * @param value
     * @return
     */
    public Boolean setNx(String key, String value) {
        return setNx(key, value, DEFAULT_EXPIRE);
    }

    /**
     * SET if Not eXists. 若key不存在，则创建该key，同时返回true；若key已存在，创建失败，返回false
     * setNx和过期时间已进行原子性操作
     * @param key
     * @param value
     * @param seconds 过期时间，若传入值小于等于0 则默认过期时间为10秒
     * @return
     */
    public Boolean setNx(String key, String value, int seconds) {
        return setNx(key, value, seconds, SETNX_DEFAULT_INDEX);
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
    public Boolean setNx(String key, String value, int seconds, int index) {
        return redisSource.setNx(key, value, seconds, index);
    }


    public String hget(String key, String field) {
        return hget(key, field, DEFAULT_INDEX);
    }

    public String hget(String key, String field, int index) {
        return redisSource.hget(key, field, index);
    }

    public Map<String, String> hget(String key) {
        return hget(key, DEFAULT_INDEX);
    }

    public Map<String, String> hget(String key, int index) {
        return redisSource.hget(key, index);
    }


    /**
     * 队列操作。 将一个或多个 value 插入到列表 key 的表头
     * 如果 key 不存在，一个空列表会被创建并执行 push 操作。
     * 当 key 存在但不是列表类型时，返回一个错误。
     *
     * @param key 队列名称
     * @return 队列长度
     */
    public Long pushToHead(String key, String... values) {
        return redisSource.pushToHead(key, values);
    }

    /**
     * 队列操作。 将一个或多个 value 插入到列表 key 的表尾
     * 如果 key 不存在，一个空列表会被创建并执行 push 操作。
     * 当 key 存在但不是列表类型时，返回一个错误。
     *
     * @param key 队列名称
     * @return 队列长度
     */
    public Long pushToTail(String key, String... values) {
        return redisSource.pushToTail(key, values);
    }


    /**
     * 队列操作。 移除并返回列表 key 的头元素。
     * 当key不存在时，返回 null
     *
     * @param key 队列名称
     * @return
     */
    public String popFromHead(String key) {

        return redisSource.popFromHead(key);
    }

    /**
     * 队列操作。 移除并返回列表 key 的尾元素。
     * 当key不存在时，返回 null
     *
     * @param key 队列名称
     * @return
     */
    public String popFromTail(String key) {
        return redisSource.popFromTail(key);
    }

    /**
     * 缓存模块获取对应key的对应值.
     * 示例：xxx:123  xxx1:123   xxxxx  此方法传入xxx 只会查询到xxx:123结构的值
     *
     * @param key
     * @param index redis库
     * @return 当返回为null时表示对应库中无该key
     */
    public Set<String> cacheKeys(String key, int index) {
        return redisSource.keys(key + "*", index);
    }

    /**
     * 获取对应key的对应值.
     *
     * @param key
     * @param index redis库
     * @return 当返回为null时表示对应库中无该key
     */
    public Set<String> keys(String key, int index) {
        return redisSource.keys(key, index);
    }
    /**
     * @Description: 判断是否存在key
     * @param key
     * @param index redis库
     * @return 如果redis库中无key,返回false,反之返回true
     * @Date: 2020/8/18
    */
    public Boolean exists(String key,int index){
        return redisSource.exists(key, index);
    }

    /**
     * @Description: 判断是否存在以hash方式存储的key,filed
     * @param key
     * @param field
     * @param index redis库
     * @return key或hash表的filed不存在返回false,反之返回true
     * @Date: 2020/8/18
     */
    public Boolean hexists(String key,String field,int index){
        return redisSource.hexists(key, field, index);
    }
}
