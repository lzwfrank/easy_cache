package pers.lzw.ecache.config.context;

/**
 * @description:
 * @author: liu.zhengwei
 * @create: 2021/04/23
 */
public abstract class StringTypeContext implements ConfigContext<String,String>{

    /**
     * 支持将制定属性对应值转换成要求的数字类型
     * @param property 属性名称
     * @param classType 数字类型
     * @param defaultValue 默认数字类型,当在转换失败时使用
     * @param <T>
     * @return
     */
    public abstract <T extends Number> T getPropertyAsNumber(String property, Class<T> classType, T defaultValue);

    /**
     * 添加制定key-value值
     * @param propertyName
     * @param propertyValue 能够是Object类型
     */
    public abstract void addProperty(String propertyName, Object propertyValue) ;

    /**
     * 添加制定key-value值
     * @param propertyName 可以是任意类型
     * @param propertyValue 可以是任意类型
     * @param options
     */
    public abstract void addProperty(Object propertyName, Object propertyValue, Object... options) ;

    @Override
    public void addProperty(String propertyName, String propertyValue) {
        addProperty(propertyName,(Object)propertyValue);
    }
}
