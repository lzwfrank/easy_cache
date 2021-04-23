package pers.lzw.ecache.support.config;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import pers.lzw.ecache.config.ConfigTemplate;
import pers.lzw.ecache.support.ValueConvertor;
import pers.lzw.ecache.support.impl.DefaultValueConvertBuild;

/**
 * @description:
 * @author: liu.zhengwei
 * @create: 2020/11/20 13:35
 */
@AllArgsConstructor
@NoArgsConstructor
public abstract class CacheBuilderConfig<T extends CacheBuilderConfig> implements ICacheBuilderConfig {

    private ValueConvertor valueConvertor = DefaultValueConvertBuild.getDefaultValueConvertor();

    private int expireSecond = 60*30;

    private int cacheIndex = 2;

    public ValueConvertor getValueConvertor() {
        return valueConvertor;
    }

    public T setValueConvertor(ValueConvertor valueConvertor) {
        this.valueConvertor = valueConvertor;
        return self();
    }

    public int getExpireSecond() {
        return expireSecond;
    }

    public T setExpireSecond(int expireSecond) {
        this.expireSecond = expireSecond;
        return self();
    }

    public int getCacheIndex() {
        return cacheIndex;
    }

    public T setCacheIndex(int cacheIndex) {
        this.cacheIndex = cacheIndex;
        return self();
    }

    private String getIsDebug() {
        return ConfigTemplate.buildDefaultConfigTemplate().getPropertyValue("trs.debug.isOpen", "0");
    }

    public abstract T self();

}
