package pers.lzw.ecache.handler.impl.refresh;

import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import pers.lzw.ecache.handler.ICacheHandler;
import pers.lzw.ecache.handler.impl.ProxyCacheHandler;
import pers.lzw.ecache.util.LockUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @description: 刷新缓存处理者
 * @author: liu.zhengwei
 * @create: 2020/12/22 16:30
 */
@Slf4j
public abstract class RefreshHandler<T> extends ProxyCacheHandler<T> {

    private List<String> keys;

    /**
     * @description: 清理缓存时，直接使用
     * @Author: liuzhengwei
     * @Date: 2020-12-22 16:33
     * @Param: []
     * @Return: java.lang.String
     */
    public List<String> getKeys() {
        return keys;
    }

    @Override
    public ICacheHandler<T> setKey(String key) {
        checkNotNull(key);
        //由于RefreshHandler允许多个key，所以这里通过,或者;进行拆分key
        keys = Arrays.asList(key.split(",|;"));
        return this;
    }

    @Override
    public Object doIntercept(Method method, Object[] objects) throws Exception {
        Object invokeResult = null;
        synchronized (LockUtils.getLockObject(method.getName())) {
            invokeResult = method.invoke(getTarget(),objects);
            List<String> keys = getKeys();
            for(String key: keys) {
                //尝试进行缓存清理
                Try.run(() -> doCacheKeyDel(key)).getOrElseGet(throwable -> {
                    log.error("delete key:{} failure", key, throwable);
                    return null;
                });
            }
        }
        return invokeResult;
    }

    public abstract void doCacheKeyDel(String key);
}
