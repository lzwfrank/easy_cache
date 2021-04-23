package pers.lzw.ecache.support.impl;

import lombok.extern.slf4j.Slf4j;
import pers.lzw.ecache.config.ConfigTemplate;
import pers.lzw.ecache.support.ValueConvertor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * *@author:wen.wen
 * *@create 2021-01-06 11:06
 **/
@Slf4j
public class DefaultValueConvertBuild {

    private static ConfigTemplate configTemplate = ConfigTemplate.buildDefaultConfigTemplate();

    private static ValueConvertor DEFAULT_VALUE_CONVERTOR = new FastJsonStringConvertor();

    private static final Map<Class<?>, ValueConvertor> convertObjMap = new ConcurrentHashMap();

    /**
     * 获取默认的valueConvertor
     * @return
     */
    public static ValueConvertor getDefaultValueConvertor() {
        String classPath = configTemplate.getPropertyValue("trs.cache.value.convert.classpath");
        try {
            return generateValueConvertor(classPath);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("构建默认valueConvertor失败,传入的classPath={}", classPath);
        }

        return DEFAULT_VALUE_CONVERTOR;
    }

    private static ValueConvertor generateValueConvertor(String classPath) throws Exception{
        if (!isNullOrEmpty(classPath)) {
            Class<?> targetClz = Class.forName(classPath);
            if (!convertObjMap.containsKey(targetClz)) {
                synchronized (DefaultValueConvertBuild.class) {
                    if (!convertObjMap.containsKey(targetClz)) {
                        ValueConvertor valueConvertor = (ValueConvertor)Class.forName(classPath).getConstructor().newInstance();
                        convertObjMap.put(targetClz, valueConvertor);
                    }
                }
            }
            return convertObjMap.get(targetClz);
        }

        return DEFAULT_VALUE_CONVERTOR;
    }
}
