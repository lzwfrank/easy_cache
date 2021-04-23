package pers.lzw.ecache.cache;

import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import pers.lzw.ecache.exception.CacheException;
import pers.lzw.ecache.support.CacheValueHolder;
import pers.lzw.ecache.support.config.CacheBuilderConfig;

import java.util.Optional;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * @description:
 * @author: liu.zhengwei
 * @create: 2020/11/20 15:05
 */
@Slf4j
public abstract class AbstractCache<T> implements BaseCache<T>{

    private Function<CacheBuilderConfig, BaseCache> buildFunc;

    public static final byte[] EMPTY_ARRAY = new byte[0];

    private static int SHORT_CACHE_EXPIRE = 5;

    public final BaseCache<T> buildCache() {
        if (buildFunc == null) {
            throw new CacheException("no buildFunc");
        }
        BaseCache<T> cache = buildFunc.apply(getConfig());
        return cache;
    }

    @Override
    public Optional<T> getValue(String key) {
        checkArgument(!isNullOrEmpty(key), "key can't be null or empty");
        Optional<CacheValueHolder<T>> cacheValueHolder = Try.of(() -> doGet(key)).getOrElseThrow(throwable -> {
            log.error("get cache error", throwable);
            return new CacheException(throwable);
        });
        return Optional.ofNullable(
                cacheValueHolder.isPresent()? cacheValueHolder.get().getValue(): null
        );
    }

    @Override
    public void put(String key, T value) {
        checkArgument(!isNullOrEmpty(key), "key can't be null or empty");
        //当value为null的时候,为了避免缓存穿透 这里缓存较短时间
        int expire = (value == null)? SHORT_CACHE_EXPIRE: getConfig().getExpireSecond();
        Try.of(() -> {
            doPut(key, value, expire);
            return null;
        }).getOrElseThrow(throwable -> {
            log.error("put cache error", throwable);
            return new CacheException(throwable);
        });

//        CacheValueHolder<T> cacheValueHolder = new CacheValueHolder(value, expire);
//        Try.of(() -> {
//            doPut(key, cacheValueHolder);
//            return null;
//        }).getOrElseThrow(throwable -> new CacheException(throwable));
    }

    @Override
    public void deleteByPre(String key) {
        checkArgument(!isNullOrEmpty(key), "key can't be null or empty");
        Try.of(() -> {
            doDeleteByPre(key);
            return null;
        }).getOrElseThrow(throwable -> {
            log.error("delete cache error", throwable);
            return new CacheException(throwable);
        });
    }

    protected T self() {
        return (T) this;
    }

    public T buildFunc(Function<CacheBuilderConfig, BaseCache> buildFunc) {
        this.buildFunc = buildFunc;
        return self();
    }

    public abstract Optional<CacheValueHolder<T>> doGet(String key);

    public abstract void doPut(String key, T value, int expireSecond);

    public abstract void doDeleteByPre(String key);
}
