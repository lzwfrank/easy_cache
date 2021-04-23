package pers.lzw.ecache.annotation;


import pers.lzw.ecache.support.CacheKeyConverter;
import pers.lzw.ecache.support.impl.Md5KeyConvert;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 缓存相关注解
 * @author liuzhengwei
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface ECacheable {

    /**
     * key值（参数值） 使用SPEL表达式写
     *
     * @return
     */
    String[] key() default "";

    /**
     * 过期时间，默认1小时
     *
     * @return
     */
    int seconds() default 3600;

    /**
     * 当满足条件时进行缓存，这里使用EL表达式拼接条件，默认进行缓存
     * Spring Expression Language (SpEL) expression used for making the method
     * caching conditional.
     */
    String condition() default "";

    /**
     * 存储的database
     *
     * @return
     */
    int cacheIndex() default 1;

    /**
     * key加密策略，默认使用md5对key进行加密
     */
    Class<? extends CacheKeyConverter> keyConvert() default Md5KeyConvert.class;

}
