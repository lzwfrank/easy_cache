package pers.lzw.ecache.builder.source;

import pers.lzw.ecache.support.config.AbstractCacheBuilderConfig;
import pers.lzw.ecache.support.config.impl.CommonCacheConfig;

/**
 * @description: 缓存构造器
 * @author: liu.zhengwei
 * @create: 2020/11/20 18:42
 */
public class CommonCacheHandlerSource extends AbstractCacheHandlerSource {

    private AbstractCacheBuilderConfig cacheConfig;

    public CommonCacheHandlerSource() {
        this.cacheConfig = new CommonCacheConfig(this);
    }

    @Override
    public AbstractCacheBuilderConfig config() {
        return cacheConfig;
    }

}
