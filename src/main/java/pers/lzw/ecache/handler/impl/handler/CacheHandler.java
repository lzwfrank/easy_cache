package pers.lzw.ecache.handler.impl.handler;

import com.alibaba.fastjson.JSONObject;
import io.vavr.control.Try;
import org.apache.commons.lang3.ArrayUtils;
import pers.lzw.ecache.handler.ICacheHandler;
import pers.lzw.ecache.handler.impl.ProxyCacheHandler;

import java.lang.reflect.Method;

/**
 * @description: 缓存相关管理处理对象
 * @author: liu.zhengwei
 * @create: 2020/12/22 16:36
 */
public abstract class CacheHandler<T> extends ProxyCacheHandler<T> {

    protected String key = null;

    @Override
    public ProxyCacheHandler<T> setKey(String key) {
        this.key = key;
        return this;
    }

    /**
     * @description:构建通用的缓存key
     * @Author: liuzhengwei
     * @Date: 2020-12-07 18:39
     * @Param: [method, objects]
     * @Return: java.lang.String
     */
    public final String getKey(Method method, Object[] objects) {
        Try<String> atry = Try.of(()-> ArrayUtils.isEmpty(objects)? ICacheHandler.DEFAULT_CACHE_KEY: JSONObject.toJSONString(objects));
        //TODO 待完善
        return ICacheHandler.buildCacheKey(method, key != null? key: atry.getOrElse(ICacheHandler.DEFAULT_CACHE_KEY));
    }

}
