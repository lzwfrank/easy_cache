package test.trs.tcache.cache;

import com.trs.common.utils.JsonUtils;
import pers.lzw.ecache.cache.impl.LocalCache;
import pers.lzw.ecache.cache.impl.RedisCache;
import pers.lzw.ecache.builder.CacheHandlerBuilder;
import pers.lzw.ecache.builder.source.ICacheHandlerSource;
import pers.lzw.ecache.support.config.CacheBuilderConfig;
import pers.lzw.ecache.support.config.impl.DefaultCacheConfig;
import pers.lzw.ecache.support.impl.FastJsonStringConvertor;
import junit.framework.TestCase;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheManager;
import test.trs.tcache.component.OutComponent;
import test.trs.tcache.component.VO.UserVO;

/**
 * @description:
 * @author: liu.zhengwei
 * @create: 2020/11/20 15:04
 */
public class NewCacheTest extends TestCase {

    @Autowired
    private ICacheHandlerSource cacheHandlerBuilder;

    private RedisCacheManager cacheManager;

    private OutComponent outComponent = new OutComponent();


    public void testNew() {
        OutComponent newProxy = CacheHandlerBuilder.build()
                .config()
                .setCacheIndex(3)
                .setExpireSecond(60*10)
                .setValueConvertor(new FastJsonStringConvertor())
                .complete()
                .proxy(outComponent, null);
        doCacheSomething(newProxy);
//        RedisSerializer
    }

    public void testMulti() {
        CacheBuilderConfig cacheBuilderConfig = new DefaultCacheConfig();
        cacheBuilderConfig.setCacheIndex(1);
        cacheBuilderConfig.setExpireSecond(60*10);
        cacheBuilderConfig.setValueConvertor(new FastJsonStringConvertor());
        OutComponent multiProxy = CacheHandlerBuilder.buildMulti()
                .config()
                .setCaches(new LocalCache(cacheBuilderConfig), new RedisCache(cacheBuilderConfig))
//                .setCaches(new RedisCache(cacheBuilderConfig))
                .complete()
                .proxy(outComponent, "test");
        doCacheSomething(multiProxy);

    }


    private void doCacheSomething(OutComponent proxy) {
        //执行方法
        UserVO userVO = proxy.getUser();
        String json = JsonUtils.toOptionalJson(userVO).get();
        System.out.printf(json);

        //再次进行执行 查看是否获取到缓存结果
        UserVO cacheUser = proxy.getUser();
        String newJson = JsonUtils.toOptionalJson(cacheUser).get();
        System.out.printf(newJson);
        Assert.assertTrue(json.equals(newJson));
    }


    @Bean
    public ICacheHandlerSource newCacheBuilder() {
       return CacheHandlerBuilder.build()
                .config()
                .setCacheIndex(3)
                .setExpireSecond(60*10)
                .setValueConvertor(new FastJsonStringConvertor())
                .complete();
    }
}
