package pers.lzw.ecache.support;

import lombok.Data;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @description: 缓存结果持有类 包裹缓存，并存储部分缓存信息
 * @author: liu.zhengwei
 * @create: 2020/11/20 18:35
 */
@Data
public final class CacheValueHolder<V> implements Serializable {

    private static final long serialVersionUID = -223275921237544958L;

    private V value;

    private int expireSecond;

    //过期时间 基于当前缓存时间和expireSecond生成
    private long expireTime;

    public CacheValueHolder() {

    }

    public CacheValueHolder(V value, int expireSecond) {
        this.value = value;
        this.expireSecond = expireSecond;
        //计算过期的时间戳
        this.expireTime = expireSecond > -1? (System.currentTimeMillis()+ TimeUnit.SECONDS.toMillis(Long.valueOf(expireSecond))): -1;
    }


}
