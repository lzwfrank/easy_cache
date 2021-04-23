package pers.lzw.ecache.annotation.handler;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import pers.lzw.ecache.annotation.ECacheable;
import pers.lzw.ecache.builder.source.ICacheHandlerSource;
import pers.lzw.ecache.handler.impl.handler.CacheHandler;
import pers.lzw.ecache.support.impl.DefaultValueConvertBuild;
import pers.lzw.ecache.support.sqel.SpelParserUtils;
import pers.lzw.ecache.util.Keyhelper;
import pers.lzw.ecache.util.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wenwen
 * Created on 2019/8/8
 */
@Component
@Aspect
@Slf4j
public class ECacheableHandler {

    @Autowired
    private ICacheHandlerSource cacheHandlerBuilder;

    @Autowired
    private CacheHandler cacheHandler;

    private static final Map<Class<?>, Object> convertObjMap = new ConcurrentHashMap();
    /**
     * 设置切入点
     */
    @Pointcut("@annotation(pers.lzw.ecache.annotation.ECacheable)")
    public void eCache() {
    }

    //获取参数名称
    private LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    /**
     * 采用环绕通知
     *
     * @param joinPoint    切入点
     * @param eCacheable
     * @return
     */
    @Around("@annotation(eCacheable)")
    public Object around(ProceedingJoinPoint joinPoint, ECacheable eCacheable) throws Throwable {
        Object invokeResult = null;
        //TODO 获取要代理的方法
        Method targetMethod = getMethod(joinPoint);

        //TODO 当需要满足一定条件才进行缓存时
        Boolean condition = SpelParserUtils.parseSpel(discoverer.getParameterNames(targetMethod), joinPoint.getArgs(), eCacheable.condition(), Boolean.class, true);
        if (!condition) {
            return joinPoint.proceed();
        }
        //TODO 构建key值
        String key = Keyhelper.buildCacheableKey(SpelParserUtils.parseSpelToString(discoverer.getParameterNames(targetMethod), joinPoint.getArgs(), eCacheable.key()));

        //TODO 对构建成功的key值进行转换
        Object convertObj = getConvertObj(eCacheable.keyConvert());
        key = StringUtils.toStringValue(eCacheable.keyConvert().getMethod("convert", String.class).invoke(convertObj, key));
        //拼接前缀和key值
//        key = eCacheable. + ":" + key;
        String cacheKey = key;
        //若没有全局配置 则使用默认的CacheHandlerBuilder
        Object proxy = cacheHandlerBuilder.setHandler(cacheHandler)
                .config()
                .setValueConvertor(DefaultValueConvertBuild.getDefaultValueConvertor())
                .setExpireSecond(eCacheable.seconds())
                .setCacheIndex(eCacheable.cacheIndex())
                .complete()
                .proxy(joinPoint.getTarget(), cacheKey);

        invokeResult = targetMethod.invoke(proxy, joinPoint.getArgs());
        return invokeResult;
    }

    /**
     * 从joinPoint 获取被代理方法
     *
     * @param joinPoint 切入点
     * @return
     */
    private Method getMethod(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        if (method.getDeclaringClass().isInterface()) {
            try {
                method = joinPoint
                        .getTarget()
                        .getClass()
                        .getDeclaredMethod(joinPoint.getSignature().getName(),
                                method.getParameterTypes());
            } catch (SecurityException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        return method;
    }

    private Object getConvertObj(Class<?> clazz) throws Throwable{
        if (convertObjMap.containsKey(clazz)) {
            return convertObjMap.get(clazz);
        }
        Constructor constructor = clazz.getConstructor();
        Object convertObj = constructor.newInstance();
        convertObjMap.put(clazz, convertObj);
        return convertObj;
    }

}
