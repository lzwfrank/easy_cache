package pers.lzw.ecache.handler;

import pers.lzw.ecache.cache.BaseCache;

import java.lang.reflect.Method;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @description:
 * @author: liu.zhengwei
 * @create: 2020/11/24 18:10
 */
public interface ICacheHandler<T> {

    String DEFAULT_CACHE_KEY = "default";

    /**
     * @description:
     * @Author: liuzhengwei
     * @Date: 2020-12-07 18:39
     * @Param: [method, objects]
     * @Return: java.lang.String
     */
    static String buildCacheKey(Method method, String key) {
        checkNotNull(key, "key can't be null or empty");
        checkNotNull(method,"method can't be null or empty");
        //TODO 待完善
        return String.format("%s:%s",getPreCachekey(method), key);
    }

    static String getPreCachekey(Method method) {
        checkNotNull(method,"method can't be null or empty");
        return getPreCachekey(method.getDeclaringClass().getName(), method.getName());
    }

    static String getPreCachekey(String className, String methodName) {
        checkNotNull(className,"className can't be null or empty");
        checkNotNull(className,"methodName can't be null or empty");
        return String.format("%s:%s",className, methodName);
    }

    T buildProxy(T target);

    BaseCache<T> getCache();

    ICacheHandler<T> setKey(String key);

    ICacheHandler<T> setCache(BaseCache cache);


}
