package pers.lzw.ecache.base;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @description:简单的lrumap
 * @author: liu.zhengwei
 * @create: 2020/12/22 17:37
 */
public class LruMap<K,V> extends LinkedHashMap<K,V> {

    private int maxSize;

    public LruMap(int size){
        super(size,0.75f,true);
        this.maxSize=size;
    };

    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return (size()>maxSize);
    }

}
