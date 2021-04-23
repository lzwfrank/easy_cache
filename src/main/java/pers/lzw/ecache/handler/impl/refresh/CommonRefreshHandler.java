package pers.lzw.ecache.handler.impl.refresh;

import pers.lzw.ecache.cache.BaseCache;

/**
 * @description:通用的缓存清理处理类
 * @author: liu.zhengwei
 * @create: 2020/12/22 16:41
 */
public class CommonRefreshHandler extends RefreshHandler {

    @Override
    public void doCacheKeyDel(String key) {
        BaseCache baseCache = getCache();
        baseCache.deleteByPre(key);
    }
}
