package pers.lzw.ecache.config.context;

/**
 * @Description:可配置接口
 * @Author: liu.zhengwei
 * @date: 2021/4/23
 */
public interface ConfigContext<K,V> {
    V getProperty(K property);

    V getProperty(K property, V defaultValue);

    Object getPropertyAsObject(K property);

    Object getPropertyAsObject(K property, Object defaultObj);

    void addProperty(K propertyName, V propertyValue);

    boolean containProperty(K property);

}
