package pers.lzw.ecache.util;

import com.alibaba.fastjson.JSONObject;
import io.vavr.control.Either;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import pers.lzw.ecache.base.EitherFunction;
import pers.lzw.ecache.redis.JedisPoolUtils;

import java.io.Serializable;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * @description:针对同一个类下无法代理的情况，提供一个灵活性较高的缓存处理模块
 * @author: liu.zhengwei
 * @create: 2020/01/14 21:57
 */
@Slf4j
public class CacheUtils {

    private static int DEFAULT_INDEX = 8;

    //默认半个小时
    private static int CACHE_EXPIRE_SECOND = 1800;

    /**
     * 数据以字节数组方式缓存.默认缓存db=8,半个小时过期
     * 注意：缓存的数据实体类必须实现序列化接口
     * @param cacheKey redis缓存key
     * @param dosomething 检索缓存数据的方法
     * @return
     */
    public static <T extends Serializable> Try<T> doCache(String cacheKey, Supplier<T> dosomething) {
        return doCache(cacheKey,CACHE_EXPIRE_SECOND, DEFAULT_INDEX, dosomething);
    }

    /**
     * 数据以字节数组方式缓存.默认缓存db=8
     * 注意：缓存的数据实体类必须实现序列化接口
     * @param cacheKey redis缓存key
     * @param expireSecond 过期时间
     * @param dosomething 检索缓存数据的方法
     * @return
     */
    public static <T extends Serializable> Try<T> doCache(String cacheKey,int expireSecond, Supplier<T> dosomething) {
        return doCache(cacheKey,expireSecond, DEFAULT_INDEX, dosomething);
    }

    /**
     * 数据以字节数组方式缓存
     * 注意：缓存的数据实体类必须实现序列化接口
     * @param cacheKey redis缓存key
     * @param expireSecond redis缓存过期时间
     * @param index redis索引
     * @param dosomething 检索缓存数据的方法
     * @return
     */
    public static <T extends Serializable> Try<T> doCache(String cacheKey, int expireSecond,
                                                             int index, Supplier<T> dosomething) {
        checkNotNull(cacheKey, "缓存key不能为空");
        checkNotNull(dosomething, "结果获取方式不能为空");
        Try<T> tryCache = Try.of(() -> {
            T result;
            byte[] cacheBytes = JedisPoolUtils.newBuilder().get(cacheKey.getBytes(), index);
            if (cacheBytes == null || cacheBytes.length == 0) {
               try{
                    result = dosomething.get();
                    if (result != null) {
                        JedisPoolUtils.newBuilder().set(cacheKey.getBytes(),
                                SerializationUtils.serialize(result),
                                expireSecond,
                                index);
                    }
                } catch (Throwable throwable) {
                    log.error(String.format("缓存失败,异常信息为：",throwable.getMessage()), throwable);
                    throw new IllegalArgumentException(throwable);
                }
            } else {
                result = SerializationUtils.deserialize(cacheBytes);
            }
            return result;
        });

        return tryCache;
    }

    /**
     * 缓存方法,若缓存中没有该数据，则直接执行getResult方法。可以选择通过getResult的结果是否需要缓存.结果将序列化为json
     * @param cacheKey 缓存的唯一key健
     * @param tClass class类型，方便进行json形式的反序列化
     * @param <T>
     * @return
     */
    public static <T> Try<T> doCacheAsJson(String cacheKey, Class<T> tClass,  EitherFunction<T> dosomething) {
        return doCacheAsJson(cacheKey,tClass,CACHE_EXPIRE_SECOND, DEFAULT_INDEX, dosomething);
    }

    /**
     * 缓存方法,若缓存中没有该数据，则直接执行getResult方法。可以选择通过getResult的结果是否需要缓存。结果将序列化为json
     * @param cacheKey 缓存的唯一key健
     * @param tClass class类型，方便进行json形式的反序列化
     * @param expireSecond 过期时间 -1表示永不过期
     * @param dosomething 获取结果的执行函数
     * @param <T>
     * @return
     */
    public static <T> Try<T> doCacheAsJson(String cacheKey, Class<T> tClass, int expireSecond,
                                           int index, EitherFunction<T> dosomething) {

        checkNotNull(cacheKey, "缓存key不能为空");
        checkNotNull(dosomething, "结果获取方式不能为空");
        //暂时让获取不到缓存
        Try<T> tTry = Try.of(() -> {
            T result = null;
            String token = JedisPoolUtils.newBuilder().get(cacheKey, index);
            if (isNullOrEmpty(token)) {
                try {
                    //进行统一结果包装
                    Either<Throwable, T> either = dosomething.getResult();
                    result = either.getOrElseThrow(Throwable::getCause);
                    if (result != null) {
                        JedisPoolUtils.newBuilder().set(cacheKey,result instanceof String? (String) result : JSONObject.toJSONString(result), expireSecond, index);
                    }
                } catch (Throwable throwable) {
                    log.error(String.format("缓存失败,异常信息为：",throwable.getMessage()), throwable);
                    throw new IllegalArgumentException(throwable);
                }
            }else {
                result =tClass.isAssignableFrom(String.class)? (T) token : JSONObject.parseObject(token, tClass);
            }
            return result;
        });
        return tTry;
    }
}
