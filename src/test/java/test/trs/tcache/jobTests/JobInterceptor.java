package test.trs.tcache.jobTests;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.concurrent.*;

/**
 * @description:
 * @author: liu.zhengwei
 * @create: 2018/11/12 16:44
 */
public class JobInterceptor implements MethodInterceptor {

    private static ExecutorService service = Executors.newFixedThreadPool(20);

    private Object target = null;

    public JobInterceptor(Object target) {
        this.target = target;
    }


    @Override
    public Object intercept(final Object o,final Method method, final Object[] objects, MethodProxy methodProxy) throws Throwable {
        Object result = null;
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }
        Future<Object> future = service.submit(new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        Object result = method.invoke(target, method);
                        return result;
                    }
                });

        try {
            result = future.get(100,TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
            future.isCancelled();
        }
        return result;
    }

    public Object getProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(this.target.getClass());
        enhancer.setCallback(this);
        return enhancer.create();
    }
}
