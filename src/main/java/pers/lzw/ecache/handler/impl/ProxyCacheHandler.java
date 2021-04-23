package pers.lzw.ecache.handler.impl;

import pers.lzw.ecache.cache.BaseCache;
import pers.lzw.ecache.cache.impl.RedisCache;
import pers.lzw.ecache.exception.CacheException;
import pers.lzw.ecache.handler.ICacheHandler;
import pers.lzw.ecache.support.config.impl.DefaultCacheConfig;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * @description:
 * @author: liu.zhengwei
 * @create: 2020/11/24 18:13
 */
@Slf4j
public abstract class ProxyCacheHandler<T> implements ICacheHandler<T>, MethodInterceptor {


    private T target = null;

    private BaseCache cache;

    private boolean debug = false;

    protected int expire = -1;

    protected static String isDebug = "0";

    @Override
    public T buildProxy(T base) {
        this.target = base;
        return !debug && isDebug.equals("0")? getProxy(): target;
    }

    @Override
    public BaseCache<T> getCache() {
        if (cache == null) {
            //默认使用redis进行缓存
            cache = new RedisCache(new DefaultCacheConfig());
        }
        return cache;
    }

    public T getProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(this.target.getClass());
        enhancer.setCallback(this);
        return (T) enhancer.create();
    }

    public ProxyCacheHandler<T> setTarget(T target) {
        this.target = target;
        return this;
    }

    @Override
    public ProxyCacheHandler<T> setCache(BaseCache cache) {
        this.cache = cache;
        return this;
    }

    public ProxyCacheHandler<T> setDebug(boolean debug) {
        this.debug = debug;
        return this;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        Object invokeResult = null;
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }
        //object本身自带方法以及非公有的方法 不会进行缓存代理
        Method baseMethod = ReflectionUtils.findMethod(Object.class, method.getName());
        if (baseMethod != null) {
            invokeResult = method.invoke(target,objects);
        }else {
            //尝试进行代理
            invokeResult = Try.of(() -> doIntercept(method, objects)).getOrElseGet((throwable) -> {
                log.error("fail to cache", throwable);
                //判断是否是缓存报错，若是则直接执行方法
                if (throwable instanceof CacheException) {
                    return Try.of(() -> method.invoke(target,objects)).getOrElseThrow(th -> new CacheException(th));
                }else {
                    throw new CacheException(throwable);
                }
            });
        }
        return invokeResult;
    }

    protected T getTarget() {
        return target;
    }

    public abstract Object doIntercept(Method method, Object[] objects) throws Exception;
}
