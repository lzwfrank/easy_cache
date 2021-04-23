package pers.lzw.ecache.annotation;

import pers.lzw.ecache.builder.CacheHandlerBuilder;
import pers.lzw.ecache.builder.source.ICacheHandlerSource;
import pers.lzw.ecache.handler.impl.handler.CacheHandler;
import pers.lzw.ecache.handler.impl.handler.CommonCacheHandler;
import pers.lzw.ecache.handler.impl.refresh.CommonRefreshHandler;
import pers.lzw.ecache.handler.impl.refresh.RefreshHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @description:缓存相关的默认配置
 * @author: liu.zhengwei
 * @create: 2020/12/22 15:52
 */
@Component
@Configuration
public class CachePostConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ICacheHandlerSource cacheHandlerBuilder() {
        return CacheHandlerBuilder.build();
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheHandler cacheHandler() {
        return new CommonCacheHandler();
    }


    @Bean
    @ConditionalOnMissingBean
    public RefreshHandler refreshHandler() {
        return new CommonRefreshHandler();
    }



}
