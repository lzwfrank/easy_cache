package pers.lzw.ecache.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * @description:反射相关方法
 * @author: liu.zhengwei
 * @create: 2020/08/31 10:25
 */
@Slf4j
public class ReflectionUtils {

    //容量为512
    private static final Map<Class<?>, Field[]> declaredFieldsCache = new ConcurrentReferenceHashMap<>(2<<8);

    private static final Field[] NO_FIELDS = {};

    public static <T> List<String> getFieldValues(List<T> datas, String fieldName) {
        checkArgument(datas.size() > 0);
        Field field = org.springframework.util.ReflectionUtils.findField(datas.get(0).getClass(), fieldName);
        checkNotNull(field, "当前类型不支持此字段此类检索");
        //获取所有的对应的原发稿件
        List<String> rootIds = datas.stream().map((msg) -> {
            try {
                field.setAccessible(true);
                String rootMsgId = field.get(msg) != null ? String.valueOf(field.get(msg)) : null;
                return rootMsgId;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                throw new IllegalArgumentException(e);
            }
        }).collect(Collectors.toList()).stream().filter((rootMsgId) -> rootMsgId != null).collect(Collectors.toList());
        return rootIds;
    }

    public static Object getFiledValue(Field field, Object target) {
        field.setAccessible(true);
        return org.springframework.util.ReflectionUtils.getField(field, target);
    }

    /**
     * 获取所有字段，包含父类
     *
     * @param searchType
     * @return
     */
    public static List<Field> getFields(Class<?> searchType) {
        List<Field> results = new LinkedList<>();
        Assert.notNull(searchType, "Class must not be null");
        while (Object.class != searchType && searchType != null) {
            Field[] fields = getDeclaredFields(searchType);
            results.addAll(Arrays.asList(fields));
            searchType = searchType.getSuperclass();
        }
        return results;
    }

    private static Field[] getDeclaredFields(Class<?> clazz) {
        Assert.notNull(clazz, "Class must not be null");
        Field[] result = declaredFieldsCache.get(clazz);
        if (result == null) {
            try {
                result = clazz.getDeclaredFields();
                declaredFieldsCache.put(clazz, (result.length == 0 ? NO_FIELDS : result));
            } catch (Throwable ex) {
                throw new IllegalStateException("Failed to introspect Class [" + clazz.getName() +
                        "] from ClassLoader [" + clazz.getClassLoader() + "]", ex);
            }
        }
        return result;
    }

    /**
     * 将传入的字符串解析成指定指定的Number类型
     * @param value 给定的字符串
     * @param typeClass 指定的类型
     * @param <T>
     * @return
     */
    public static <T extends Number> T parseNumber(String value, Class<T> typeClass) {
        if(isNullOrEmpty(value))
            return null;
        final String clearNumber = value.trim();
        if(typeClass.equals(Byte.class))
            return (T) (Byte.valueOf(clearNumber));
        else if(typeClass.equals(Short.class))
            return (T) (Short.valueOf(clearNumber));
        else if(typeClass.equals(Integer.class))
            return (T) (Integer.valueOf(clearNumber));
        else if(typeClass.equals(Long.class))
            return (T) (Long.valueOf(clearNumber));
        else if(typeClass.equals(Double.class))
            return (T) (Double.valueOf(clearNumber));
        else
            throw new IllegalArgumentException(String.format("[%s] can not be converted into [%s]", clearNumber, typeClass.getName()));
    }

}
