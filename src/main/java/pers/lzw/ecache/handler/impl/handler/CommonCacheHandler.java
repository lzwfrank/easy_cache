package pers.lzw.ecache.handler.impl.handler;

import pers.lzw.ecache.cache.BaseCache;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * @description: 当前默认代理模式下的通用缓存处理者.
 * @author: liu.zhengwei
 * @create: 2020/11/24 18:35
 */
public class CommonCacheHandler<T> extends CacheHandler<T> {

    @Override
    public Object doIntercept(Method method, Object[] objects) throws Exception{
        Object invokeResult = null;
        BaseCache baseCache = getCache();
        //TODO 存储key健为 key:方法名
        String currentKey =getKey(method,objects);
        Optional<Object> optionalS = baseCache.getValue(currentKey);
        if(!optionalS.isPresent()) {
            invokeResult = method.invoke(getTarget(),objects);
            //TODO 更新缓存
            baseCache.put(currentKey, invokeResult);
        }
        return invokeResult != null? invokeResult: optionalS.get();
    }
}
