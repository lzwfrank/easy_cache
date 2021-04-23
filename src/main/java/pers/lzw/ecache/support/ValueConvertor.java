package pers.lzw.ecache.support;

import java.util.Optional;

/**
 * @description:结果转换器 对缓存结果进行序列化和反序列化
 * @author: liu.zhengwei
 * @create: 2020/11/20 11:03
 */
public interface ValueConvertor {

    byte[] EMPTY_ARRAY = new byte[0];

    <T> Optional<byte[]> encode(CacheValueHolder<T> value);

    <T> CacheValueHolder<T> decode(byte[] cacheValue);
}
