package pers.lzw.ecache.support;

import java.util.function.Function;

/**
 * @program media_base_cache
 * @author: liu.zhengwei
 * @create: 2020/12/07 18:52
 */
public interface KeyConvertor extends Function<Object, String> {

    @Override
    String apply(Object t);
}
