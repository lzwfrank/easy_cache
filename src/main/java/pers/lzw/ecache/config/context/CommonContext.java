package pers.lzw.ecache.config.context;


import com.google.common.base.Defaults;
import com.google.common.reflect.Reflection;
import org.apache.commons.lang3.StringUtils;
import pers.lzw.ecache.util.ReflectionUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static pers.lzw.ecache.util.StringUtils.toStringValue;

/**
 * 通用的上下文
 * Created by cy on 2015/11/28.
 */
public class CommonContext extends StringTypeContext implements Serializable {

    private static final long serialVersionUID = 362498820763181265L;

    private final Map<String,Object> COMMON_CONTEXT = new HashMap<String, Object>();

//    private static final Object EMPTY_OBJECT = new String("");

    public CommonContext(){
    }

    public CommonContext(CommonContext context){
        if (context != null) {
            for (Map.Entry<String,Object> entry : context.toMap().entrySet()) {
                this.COMMON_CONTEXT.put(entry.getKey(),entry.getValue());
            }
        }
    }

    public CommonContext(Map<String, String[]> context){
        if (null != context)convertContext(context);

    }

    public static CommonContext of(CommonContext context){
        CommonContext commonContext = new CommonContext();
        if (context != null) {
            for (Map.Entry<String,Object> entry : context.toMap().entrySet()) {
                commonContext.COMMON_CONTEXT.put(entry.getKey(),entry.getValue());
            }
        }
        return commonContext;
    }

    public CommonContext(Map<String, String[]> context, String separator) {
        if (null != context)convertContext(context, separator);
    }

    /**
     * 将map转化为commonContext，如果value有多个值,默认将多个值用分号隔开
     * @param map
     * @return
     */
    public void convertContext(Map<String, String[]> map){
        mapToContext(map, ";");
    }

    /**
     * 将map转化为commonContext，如果value有多个值就将多个值用分隔符隔开
     * @param map
     * @param separator
     * @return
     */
    public void convertContext(Map<String, String[]> map, String separator) {
        mapToContext(map, isNullOrEmpty(separator) ? ";" : separator);
    }

    /**
     * 将map转化为commonContext，如果value有多个值就将多个值用分隔符隔开
     * @param map
     * @param separator
     * @return
     */
    private void mapToContext(Map<String, String[]> map, String separator) {
        if (null != map && !map.isEmpty()){
            for (Map.Entry<String, String[]> entry: map.entrySet()){
                String value = Defaults.defaultValue(String.class);
                if (entry.getValue() != null) {
                    if (entry.getValue().length == 1){
                        value = entry.getValue()[0];
                    } else if(entry.getValue().length > 1) {
                        value = StringUtils.join(entry.getValue(), separator);
                    }
                }
                COMMON_CONTEXT.put(entry.getKey(), value);
            }
        }
    }

    /**
     * 删除上下文中指定属性,如果属性不存在,则什么都不操作
     * @param property
     */
    public void removeProperty(String property) {
        if(isNullOrEmpty(property) || !COMMON_CONTEXT.containsKey(property))
            return;
        COMMON_CONTEXT.remove(property);
    }

    /**
     * 将缓存按照Map形式进行返回
     * @return
     */
    public Map<String,Object> toMap() {
        return COMMON_CONTEXT;
    }

    public Map<String,Object> toMap(String... properties) {
        checkNotNull(properties, "the chosen properties are emtpy !");
        Map<String,Object> chosenPropertiesMap = new HashMap<>();
        for(String property : properties)
            chosenPropertiesMap.put(property, COMMON_CONTEXT.get(property));
        return chosenPropertiesMap;
    }

    /**
     * 清除整个上下文
     */
    public void clearContext() {
        COMMON_CONTEXT.clear();
    }

    @Override
    public String getProperty(String property) {
        return toStringValue(getPropertyAsObject(property));
    }

    @Override
    public String getProperty(String property, String defaultValue) {

        return COMMON_CONTEXT.containsKey(property)
                ? (isEmpty(toStringValue(COMMON_CONTEXT.get(property))) ? defaultValue : toStringValue(COMMON_CONTEXT.get(property)))
                : defaultValue;
    }

    @Override
    public Object getPropertyAsObject(String property) {
        return COMMON_CONTEXT.get(property);
    }

    @Override
    public Object getPropertyAsObject(String property, Object defaultObj) {

        return COMMON_CONTEXT.containsKey(property) ?
                (COMMON_CONTEXT.get(property) == null ? defaultObj : COMMON_CONTEXT.get(property))
                : defaultObj;
    }


    @Override
    public <T extends Number> T getPropertyAsNumber(String property, Class<T> classType, T defaultValue) {
        checkArgument(classType.isPrimitive() || Number.class.isAssignableFrom(classType), "classType必须指定为基础/Number类型");

        // 基本类型转包装类
        classType = numberBaseClassToPackagingClass(classType);

        String value = getProperty(property);
        try {
            if(isEmpty(value)) {
                return defaultValue;
            }

            return (T) ReflectionUtils.parseNumber(value,classType);
        } catch (ClassCastException ccex) {
            throw ccex;
        }
    }

    // 数字基本类型转包装类
    private Class numberBaseClassToPackagingClass(Class tClass) {
        if (tClass.isPrimitive()) {
            if (tClass == Short.TYPE) {
                return Short.class;
            } else if (tClass == Integer.TYPE) {
                return Integer.class;
            } else if (tClass == Long.TYPE) {
                return Long.class;
            } else if (tClass == Float.TYPE) {
                return Float.class;
            } else if (tClass == Double.TYPE) {
                return Double.class;
            } else {
                throw new IllegalArgumentException("classType必须指定为基础/Number类型");
            }
        }

        return tClass;
    }

    @Override
    public void addProperty(String propertyName, Object propertyValue) {
        COMMON_CONTEXT.put(checkNotNull(propertyName),propertyValue);
    }

    @Override
    public void addProperty(Object propertyName, Object propertyValue, Object... options) {
        addProperty(String.valueOf(propertyName), propertyValue);
    }

    @Override
    public boolean containProperty(String property) {
        if (isNullOrEmpty(property))
            return false;
        return COMMON_CONTEXT.containsKey(property);
    }
}
