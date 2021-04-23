package pers.lzw.ecache.support.config;

import pers.lzw.ecache.cache.BaseCache;
import pers.lzw.ecache.builder.source.ICacheHandlerSource;

/**
 *
 * @program media_base_cache
 * @author: liu.zhengwei
 * @create: 2020/12/11 14:39
 */
public interface ICacheBuilderConfig {

    <T> BaseCache<T> buildCache();

    ICacheHandlerSource complete();

}
