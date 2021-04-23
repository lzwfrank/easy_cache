package pers.lzw.ecache.builder;

import pers.lzw.ecache.builder.source.CommonCacheHandlerSource;
import pers.lzw.ecache.builder.source.ICacheHandlerSource;
import pers.lzw.ecache.builder.source.MultiLevelCacheHandlerSource;
import pers.lzw.ecache.handler.ICacheHandler;
import pers.lzw.ecache.support.config.AbstractCacheBuilderConfig;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @description: 缓存构造器 包装
 * @author: liu.zhengwei
 * @create: 2020/11/20 18:42
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CacheHandlerBuilder implements ICacheHandlerSource {

    private ICacheHandlerSource source;

    public static CacheHandlerBuilder build() {
        CacheHandlerBuilder cacheHandlerBuilder = new CacheHandlerBuilder();
        cacheHandlerBuilder.source = new CommonCacheHandlerSource();
        return cacheHandlerBuilder;
    }

    public static MultiLevelCacheHandlerSource buildMulti() {
        MultiLevelCacheHandlerSource cacheHandlerBuilder = new MultiLevelCacheHandlerSource();
        return cacheHandlerBuilder;
    }

    @Override
    public AbstractCacheBuilderConfig config() {
        return source.config();
    }

    @Override
    public <T> T proxy(T target, String key) {
        return source.proxy(target, key);
    }

    @Override
    public <T> ICacheHandlerSource setHandler(ICacheHandler<T> cacheHandler) {
        return source.setHandler(cacheHandler);
    }
}
