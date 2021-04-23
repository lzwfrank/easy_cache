package pers.lzw.ecache.support.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.IOUtils;
import pers.lzw.ecache.support.CacheValueHolder;
import pers.lzw.ecache.support.ValueConvertor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * @description: 基于media_base的jsonUtils方法进行 json序列化和反序列化
 * @author: liu.zhengwei
 * @create: 2020/11/20 11:10
 */
@Slf4j
public class FastJsonStringConvertor implements ValueConvertor {

    private static ParserConfig defaultConfig = new ParserConfig();

    static {
        ParserConfig.getGlobalInstance().addAccept("pers");
        ParserConfig.getGlobalInstance().addAccept("com");
        defaultConfig.setAutoTypeSupport(true);
    }


    @Override
    public <T> Optional<byte[]> encode(CacheValueHolder<T> value) {
        byte[] cacheResult ;
        //只针对String(json)结构或者能直接进行转json转化的数据进行缓存
        if (value == null) {
            return Optional.empty();
        }
        cacheResult = JSON.toJSONBytes(value, new SerializerFeature[]{SerializerFeature.WriteClassName});
        return Optional.of(cacheResult);
    }

    @Override
    public <T> CacheValueHolder<T> decode(byte[] cacheResult) {
        CacheValueHolder<T> cacheValueHolder = JSON.parseObject(new String(cacheResult, IOUtils.UTF8), CacheValueHolder.class, defaultConfig, new Feature[0]);
        return cacheValueHolder;
    }
}
