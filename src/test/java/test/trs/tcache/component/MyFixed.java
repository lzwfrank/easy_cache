package test.trs.tcache.component;

import net.sf.cglib.proxy.FixedValue;

/**
 * @description:
 * @author: liu.zhengwei
 * @create: 2018/11/12 00:32
 */
public class MyFixed implements FixedValue{

    /**
     * 该类实现FixedValue接口
     * 可以在实现的方法loadObject中指定返回固定值，而不调用目标类函数
     */
    @Override
    public Object loadObject() throws Exception {
        System.out.println("锁定结果");
        Object obj = 999;
        return obj;
    }
}
