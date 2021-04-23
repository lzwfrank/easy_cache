package test.trs.tcache.component;

import net.sf.cglib.proxy.CallbackFilter;

import java.lang.reflect.Method;

/**
 * @description:
 * @author: liu.zhengwei
 * @create: 2018/11/12 00:15
 */
public class TargetMethodCallbackFilter implements CallbackFilter {

    @Override
    public int accept(Method method) {
        if (method.getName().equals("method1")) {
            System.out.println("filter method1 ==0");
            return 0;
        } else if (method.getName().equals("method2")) {
            System.out.println("filter method2 ==1");
            return 1;
        }
        return 2;
    }

}
