package pers.lzw.ecache.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 缓存更新标记 当执行被标记方法时 会对配置的class下所有方法的缓存进行更新。也可以精确到具体方法
 *  默认刷新当前类下面的所有方法
 * @program media_base_cache
 * @author: liu.zhengwei
 * @create: 2020/12/15 15:18
 */
@Target(ElementType.METHOD)
@Component
@Retention(RetentionPolicy.RUNTIME)
public @interface ERefresh {

    //需要更新的class
    RefreshClass[] refreshClasses() default {};

    /**
     * 当满足条件时进行缓存，这里使用EL表达式拼接条件，默认进行缓存
     * Spring Expression Language (SpEL) expression used for making the method
     * caching conditional.
     */
    String condition() default "";

}
