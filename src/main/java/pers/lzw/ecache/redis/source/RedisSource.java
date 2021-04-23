package pers.lzw.ecache.redis.source;

import java.util.Map;
import java.util.Set;

/**
 * @description: redis相关获取信息
 * @author: liu.zhengwei
 * @create: 2020/12/18 10:20
 */
public interface RedisSource {

    /**
     * 向redis中存入值
     *
     * @param key
     * @param value
     * @param expireSecond 过期时间，当为-1（小于0）时 表示不设置过期时间
     * @param index        redis库
     */
    void set(String key, String value, int expireSecond, int index);

    /**
     * 获取对应key的对应值
     *
     * @param key
     * @param index redis库
     * @return 当返回为null时表示对应库中无该key
     */
    String get(String key, int index);

    String hget(String key, String field, int index);

    Map<String, String> hget(String key, int index);

    void set(byte[] key, byte[] value, int expireSecond, int index);

    byte[] get(byte[] key, int index);

    /**
     * 以hash的方式进行存储
     *
     * @param key
     * @param field
     * @param value
     */
    void hset(String key, String field, String value, int index);

    /**
     * 以hash的方式进行存储
     *
     * @param key
     * @param hash
     */
    void hset(String key, Map<String, String> hash, int index);

    /**
     * 以hash的方式进行存储删除
     *
     * @param key
     * @param index
     * @param fields
     */
    void hdel(String key, int index, String... fields);

    /**
     * 删除redis中的可以键
     *
     * @param key
     * @param index redis库
     */
    void del(String key, int index);

    /**
     * SET if Not eXists. 若key不存在，则创建该key，同时返回true；若key已存在，创建失败，返回false
     * setNx和过期时间已进行原子性操作
     * @param key
     * @param value
     * @param seconds 过期时间，若传入值小于等于0 则默认过期时间为10秒
     * @param index   redis索引库
     * @return
     */
    Boolean setNx(String key, String value, int seconds, int index);

    /**
     * @Description: 判断是否存在key
     * @param key
     * @param index redis库
     * @return 如果redis库中无key,返回false,反之返回true
     * @Date: 2020/8/18
     */
    Boolean exists(String key,int index);

    /**
     * @Description: 判断是否存在以hash方式存储的key,filed
     * @param key
     * @param field
     * @param index redis库
     * @return key或hash表的filed不存在返回false,反之返回true
     * @Date: 2020/8/18
     */
    Boolean hexists(String key,String field,int index);

    /**
     * 获取对应key的对应值.
     *
     * @param key
     * @param index redis库
     * @return 当返回为null时表示对应库中无该key
     */
    Set<String> keys(String key, int index);


    /**
     * 队列操作。 移除并返回列表 key 的尾元素。
     * 当key不存在时，返回 null
     *
     * @param key 队列名称
     * @return
     */
    String popFromTail(String key);

    /**
     * 队列操作。 移除并返回列表 key 的头元素。
     * 当key不存在时，返回 null
     *
     * @param key 队列名称
     * @return
     */
    String popFromHead(String key);

    /**
     * 队列操作。 将一个或多个 value 插入到列表 key 的表尾
     * 如果 key 不存在，一个空列表会被创建并执行 push 操作。
     * 当 key 存在但不是列表类型时，返回一个错误。
     *
     * @param key 队列名称
     * @return 队列长度
     */
    Long pushToTail(String key, String... values);

    /**
     * 队列操作。 将一个或多个 value 插入到列表 key 的表头
     * 如果 key 不存在，一个空列表会被创建并执行 push 操作。
     * 当 key 存在但不是列表类型时，返回一个错误。
     *
     * @param key 队列名称
     * @return 队列长度
     */
    Long pushToHead(String key, String... values);

    /**
     * 获取 key 剩余过期时间
     * @param key
     * @return
     */
    Long getExpire(String key, int index);
}
