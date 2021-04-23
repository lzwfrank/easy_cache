package pers.lzw.ecache.builder.source;

import pers.lzw.ecache.support.config.impl.MultilevelCacheConfig;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @description: 多级缓存构造器
 * @author: liu.zhengwei
 * @create: 2020/12/14 16:31
 */
public class MultiLevelCacheHandlerSource extends AbstractCacheHandlerSource {

    private MultilevelCacheConfig cacheConfig;

    @Override
    public MultilevelCacheConfig config() {
        if (cacheConfig == null) {
            cacheConfig = new MultilevelCacheConfig(this);
        }
        checkArgument(cacheConfig instanceof MultilevelCacheConfig, "MultiLevelCacheHandlerSource只支持MultilevelCacheConfig");
        return (MultilevelCacheConfig)cacheConfig;
    }

}
