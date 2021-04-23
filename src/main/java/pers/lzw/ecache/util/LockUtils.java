package pers.lzw.ecache.util;


import pers.lzw.ecache.base.LruMap;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @description: 通用锁相关工具
 * @author: liu.zhengwei
 * @create: 2020/01/17 17:28
 */
public class LockUtils {

    public static final Lock UPLOAD_LOCK = new ReentrantLock(); //上传流程锁

    private static ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    //考虑本身是为了解决高并发上瞬时的相同数据的操作，所以1000的长度即可
    private static LruMap<Object,Object> lockKey = new LruMap<>(1024*3);

    /**
     * 颗粒锁锁对象获取。 若传入对象未被锁过，将或保存此对象
     * @param object 锁对象
     * @return
     */
    public synchronized static Object getLockObject(Object object) {
        Object result = null;
        //containsKey是比较值的hash
        if (lockKey.containsKey(object)) {
            result = lockKey.get(object);
        }else {
            //key是值的hash value是对象
            lockKey.put(object,object);
            result = object;
        }
        return result;
    }


    /**
     * @description: 提供简单的实现包装
     * @Author: liuzhengwei
     * @Date: 2020-12-22 17:46
     * @Param: [key, doSomething]
     * @Return: T
     */
    public static <T> T doWithLock(Object key, Supplier<T> doSomething) {
        checkNotNull(key);
        checkNotNull(doSomething);
        synchronized (getLockObject(key)) {
            return doSomething.get();
        }
    }

}
