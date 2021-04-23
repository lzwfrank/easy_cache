package pers.lzw.ecache.redis.source;

import io.vavr.CheckedFunction0;
import io.vavr.control.Try;
import pers.lzw.ecache.redis.builder.JedisConf;
import pers.lzw.ecache.util.StringUtils;

import java.util.HashMap;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @description: redisSource构建工厂
 * @author: liu.zhengwei
 * @create: 2020/12/18 11:13
 */
public class RedisSourceFactory {

    //默认大小2的6次方 128。 考虑不太可能存在n多个redis实例的情况
    public static HashMap<String, RedisSource> declaredInstanceCache = new HashMap<>(2<<6);

    public static RedisSource newSource() {
        RedisSource redisSource = null;
        //判断是否有配置spring.redis.master,若其值不为空，则认为是哨兵模式
        JedisConf jedisConf = JedisConf.init();
        //获取当前的redisSource类型
        Class<? extends RedisSource> aClass = jedisConf.getRedisMode().getResource();
        checkNotNull(aClass, "redisMode对应的redisSource不能为null");
        //对于JedisPoolSource的类 需要传入jedisConf进行构建
        if (JedisPoolSource.class.isAssignableFrom(aClass)) {
            redisSource =  Try.of(
                    () -> getIfAbsent(getUniqueId(aClass, jedisConf), () -> aClass.getConstructor(JedisConf.class).newInstance(jedisConf))
            ).getOrElseThrow(throwable -> new IllegalArgumentException(throwable));
        }else {
            redisSource = Try.of(
                    () -> getIfAbsent(getUniqueId(aClass, jedisConf),() -> aClass.newInstance())
            ).getOrElseThrow(throwable -> new IllegalArgumentException(throwable));
        }
        return redisSource;
    }

    /**
     * @description: 获取缓存的RedisSource 若不存在 则通过${doSomething}进行创建
     * @Author: liuzhengwei
     * @Date: 2020-12-18 17:28
     * @Param: [id, doSomething]
     * @Return: com.trs.tcache.util.redis.source.RedisSource
     */
    private static RedisSource getIfAbsent(String id, CheckedFunction0<RedisSource> doSomething) throws Throwable{
        checkNotNull(doSomething);
        if (!declaredInstanceCache.containsKey(id)) {
            synchronized (RedisSourceFactory.class) {
                if (!declaredInstanceCache.containsKey(id)) {
                    RedisSource redisSource = doSomething.apply();
                    declaredInstanceCache.put(id, redisSource);
                }
            }
        }
        return declaredInstanceCache.get(id);

    }

    private static String getUniqueId(Class<? extends RedisSource> aClass, JedisConf jedisConf) {
        checkNotNull(aClass);
        checkNotNull(jedisConf);
        String className = aClass.getName();
        return StringUtils.convertToMD5(className+jedisConf.getUniqueId());
    }
}
