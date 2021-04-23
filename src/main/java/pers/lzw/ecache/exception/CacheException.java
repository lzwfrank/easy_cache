package pers.lzw.ecache.exception;

/**
 * @description: 缓存相关异常
 * @author: liu.zhengwei
 * @create: 2020/12/08 16:57
 */
public class CacheException extends RuntimeException {

    public CacheException(Throwable throwable) {
        super(throwable);
    }

    public CacheException(String message) {
        super(message);
    }
}
