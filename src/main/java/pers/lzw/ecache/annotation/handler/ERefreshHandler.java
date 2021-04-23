package pers.lzw.ecache.annotation.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pers.lzw.ecache.annotation.ERefresh;
import pers.lzw.ecache.annotation.RefreshClass;
import pers.lzw.ecache.builder.source.ICacheHandlerSource;
import pers.lzw.ecache.handler.ICacheHandler;
import pers.lzw.ecache.handler.impl.refresh.RefreshHandler;
import pers.lzw.ecache.support.sqel.SpelParserUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * @description: @TrsCacheRefresh的具体实现。优先进行缓存清理操作
 * @Author: liuzhengwei
 * @Date: 2020-12-22 15:27
 * @Param:
 * @Return:
 */
@Component
@Aspect
@Slf4j
@Order(1)
public class ERefreshHandler {

    @Autowired
    private ICacheHandlerSource cacheHandlerBuilder;

    @Autowired
    public RefreshHandler refreshHandler;

    /**
     * 设置切入点
     */
    @Pointcut("@annotation(pers.lzw.ecache.annotation.ERefresh)")
    public void refreshCache() {
    }

    //获取参数名称
    private LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    /**
     * 采用环绕通知
     * @param joinPoint    切入点
     * @param trsCacheRefresh
     * @return
     */
    @Around("@annotation(trsCacheRefresh)")
    public Object around(ProceedingJoinPoint joinPoint, ERefresh trsCacheRefresh) throws Throwable {
        Object invokeResult = null;
        //TODO 获取要代理的方法
        Method targetMethod = getMethod(joinPoint);

        //TODO 当需要满足一定条件才进行清理
        Boolean condition = SpelParserUtils.parseSpel(discoverer.getParameterNames(targetMethod), joinPoint.getArgs(), trsCacheRefresh.condition(), Boolean.class, true);
        if (!condition) {
            return joinPoint.proceed();
        }
        RefreshClass[] refreshClasses = trsCacheRefresh.refreshClasses();

        //若没配置 则清理当前方法的缓存
        List<String> cacheKeys = ArrayUtils.isEmpty(refreshClasses)?
               Arrays.asList(ICacheHandler.getPreCachekey(targetMethod)):
                getCacheKeysByRefreshClasses(refreshClasses) ;
        //若没有全局配置 则使用默认的CacheHandlerBuilder
        Object proxy = cacheHandlerBuilder.setHandler(refreshHandler)
                .proxy(joinPoint.getTarget(), StringUtils.join(cacheKeys.toArray(), ","));

        invokeResult = targetMethod.invoke(proxy, joinPoint.getArgs());
        return invokeResult;
    }


    public List<String> getCacheKeysByRefreshClasses(RefreshClass[] refreshClasses) {
        List<String> list = new ArrayList<>();
        for (RefreshClass clazz: refreshClasses) {
            list.addAll(addCacheKeysByRefreshclass(list, clazz));
        }
        return list;
    }

    private List<String> addCacheKeysByRefreshclass(List<String> cacheKeys, RefreshClass refreshClasses) {
        String[] methodes = refreshClasses.methodName();
        String classname = refreshClasses.clazz().getName();
        if (ArrayUtils.isEmpty(methodes)) {
            cacheKeys.add(classname);
        }else {
            Arrays.stream(methodes).forEach(method -> {
                if (!isNullOrEmpty(method)) {
                    String cacheKeyPre = ICacheHandler.getPreCachekey(classname, method);
                    cacheKeys.add(cacheKeyPre);
                }
            });
        }
        return cacheKeys;
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

}
