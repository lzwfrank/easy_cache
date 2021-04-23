package pers.lzw.ecache.builder.source;

import pers.lzw.ecache.handler.ICacheHandler;
import pers.lzw.ecache.handler.impl.handler.CommonCacheHandler;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @description:
 * @author: liu.zhengwei
 * @create: 2020/12/22 15:58
 */
public abstract class AbstractCacheHandlerSource implements ICacheHandlerSource {

    protected ICacheHandler cacheHandler;

    @Override
    public <T> T proxy(T target, String key) {
        checkNotNull(config(), "缓存配置不能为空");
        ICacheHandler<T> iCacheHandler = (cacheHandler == null)? new CommonCacheHandler(): cacheHandler;
        return iCacheHandler
                .setKey(key)
                .setCache(config().buildCache())
                .buildProxy(target);
    }

    @Override
    public <T> ICacheHandlerSource setHandler(ICacheHandler<T> cacheHandler) {
        this.cacheHandler = cacheHandler;
        return this;
    }

}
