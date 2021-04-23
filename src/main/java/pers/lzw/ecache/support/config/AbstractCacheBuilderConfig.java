package pers.lzw.ecache.support.config;

import pers.lzw.ecache.builder.source.ICacheHandlerSource;

/**
 * @description:
 * @author: liu.zhengwei
 * @create: 2020/12/11 16:22
 */
public abstract class AbstractCacheBuilderConfig<T extends AbstractCacheBuilderConfig> extends CacheBuilderConfig<T> implements ICacheBuilderConfig {

    private ICacheHandlerSource cacheHandler;

    public AbstractCacheBuilderConfig(ICacheHandlerSource cacheHandler) {
        super();
        this.cacheHandler = cacheHandler;
    }

    @Override
    public ICacheHandlerSource complete() {
        return cacheHandler;
    }

}
