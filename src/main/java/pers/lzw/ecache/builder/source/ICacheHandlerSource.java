package pers.lzw.ecache.builder.source;

import pers.lzw.ecache.handler.ICacheHandler;
import pers.lzw.ecache.support.config.AbstractCacheBuilderConfig;

/**
 * @program media_base_cache
 * @author: liu.zhengwei
 * @create: 2020/12/14 16:33
 */
public interface ICacheHandlerSource {

    AbstractCacheBuilderConfig config();

    /**
     * @description: 使用默认的方式来获取代理结果。默认使用ProxyCacheHandler
     * @Author: liuzhengwei
     * @Date: 2020-11-25 15:59
     * @Param: [target, key]
     * @Return: T
     */
     <T> T proxy(T target, String key);

    <T> ICacheHandlerSource setHandler(ICacheHandler<T> cacheHandler);
}
