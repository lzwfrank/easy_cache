package pers.lzw.ecache.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description: 标记需要更新的类和方法名
 * @Author: liuzhengwei
 * @Date: 2020-12-22 18:18
 * @Param:
 * @Return:
 */
@Target(ElementType.METHOD)
@Component
@Retention(RetentionPolicy.RUNTIME)
public @interface RefreshClass {

    Class<?> clazz();

//    /**
//     * @description: 考虑存在跨系统刷新的情况 所以允许直接写包或class路径。如：com.trs.tcache.handler
//     *      和clazz互斥，只有当clazz没有值的时候才会生效
//     * @Author: liuzhengwei
//     * @Date: 2020-12-22 18:45
//     * @Param: []
//     * @Return: java.lang.String
//     */
//    String refreshPath() default "";

    /**
     * @description: 需要更新的方法名
     * @Author: liuzhengwei
     * @Date: 2020-12-22 18:29
     * @Param: []
     * @Return: java.lang.String[]
     */
    String[] methodName() default {};
}
