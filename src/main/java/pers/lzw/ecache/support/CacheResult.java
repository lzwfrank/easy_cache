package pers.lzw.ecache.support;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * @description: 缓存结果持有类
 * @author: liu.zhengwei
 * @create: 2020/12/14 19:03
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CacheResult<T> {

    private CacheCode cacheCode;

    private T value;

    public CacheCode getCacheCode() {
        return cacheCode;
    }

    public CacheResult<T> setCacheCode(CacheCode cacheCode) {
        this.cacheCode = cacheCode;
        return this;
    }

    public T getValue() {
        return value;
    }

    public CacheResult<T> setValue(T value) {
        this.value = value;
        return this;
    }

    public boolean isSuccess() {
        return cacheCode != null && cacheCode == CacheCode.success;
    }

    public static CacheResult noCache() {
        return CacheResult.builder().cacheCode(CacheCode.none).build();
    }




    public enum CacheCode {
        success,fail,none
    }
}
