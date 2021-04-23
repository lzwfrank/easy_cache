package test.trs.tcache;

import com.trs.common.utils.JsonUtils;
import pers.lzw.ecache.redis.JedisPoolUtils;
import org.junit.Assert;
import org.junit.Test;
import test.trs.tcache.component.OutComponent;
import test.trs.tcache.component.VO.UserVO;

import java.util.Set;

public class JedisPoolTests {


    @Test
    public void test(){
        String detailKey = Keyhelper.buildDetailKey("1231231");
        Set<String> keys = JedisPoolUtils.newBuilder().keys(detailKey,1);
        String list = Keyhelper.buildListKeyNopage("nava","2019121","001");
        System.out.printf("success");
    }

    @Test
    public void sentinelTest() {
        //sentinel地址集合
        JedisPoolUtils jedisPoolUtils = JedisPoolUtils.newSentinel("mymaster","172.16.25.25:26379,172.16.25.28:26379,172.16.25.42:26379","trs@admin");
        getAndSetTest(jedisPoolUtils);
        popTest(jedisPoolUtils);
        serializeTest(jedisPoolUtils);


        JedisPoolUtils single = JedisPoolUtils.newBuilder("172.16.25.62", "6379", "CmvlFfgCCQ5u7J6WJ7");
        getAndSetTest(single);
        popTest(single);
        serializeTest(single);
    }

    @Test
    public void cacheTests() {
        //sentinel地址集合
        JedisPoolUtils jedisPoolUtils = JedisPoolUtils.newBuilder("192.168.110.192","6379","");

    }

    private void getAndSetTest(JedisPoolUtils jedisPoolUtils) {
        jedisPoolUtils.set("getAndSetTest", "getAndSetTest", 5, 6);
        String value = jedisPoolUtils.get("getAndSetTest",6);
        Assert.assertTrue(value.equals("getAndSetTest"));
        try {
            Thread.sleep(5*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String str = jedisPoolUtils.get("getAndSetTest",6);
        Assert.assertTrue(str == null);
    }

    private void popTest(JedisPoolUtils jedisPoolUtils) {
        jedisPoolUtils.pushToTail("list","a","b","c");
        System.out.println(jedisPoolUtils.popFromHead("list"));
        System.out.println(jedisPoolUtils.popFromHead("list"));
        System.out.println(jedisPoolUtils.popFromHead("list"));
        System.out.println(jedisPoolUtils.popFromHead("list"));
        System.out.println("success");
    }

    private void serializeTest(JedisPoolUtils jedisPoolUtils) {
        OutComponent outComponent = new OutComponent();
        UserVO userVO = outComponent.getUser();
        jedisPoolUtils.setAsSerialize("test", userVO, 60, 6);
        UserVO cacheUser = jedisPoolUtils.getAsSerialize("test",6);
        Assert.assertTrue(JsonUtils.toOptionalJson(userVO).get().equals(JsonUtils.toOptionalJson(cacheUser).get()));
    }
}
