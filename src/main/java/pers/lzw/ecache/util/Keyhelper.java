package pers.lzw.ecache.util;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.join;

/**
 * @description: 主要辅助构建key值
 * @author: liu.zhengwei
 * @create: 2018/12/10 14:45
 */
public class Keyhelper {

    /**
     * 构建通过注解获取的key值
     *
     * @return
     */
    public static String buildCacheableKey(String value, List<String> keys) {
        return value + ":" + join(keys.toArray(), ":");
    }

    public static String buildCacheableKey(List<String> keys) {
        return join(keys.toArray(), ":");
    }
}