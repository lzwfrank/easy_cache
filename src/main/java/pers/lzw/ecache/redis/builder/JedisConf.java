package pers.lzw.ecache.redis.builder;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import pers.lzw.ecache.config.ConfigTemplate;
import pers.lzw.ecache.util.ReflectionUtils;
import pers.lzw.ecache.util.StringUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Defaults.defaultValue;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * @description:jedis配置信息类
 * @author: liu.zhengwei
 * @create: 2019/05/21 17:05
 */
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JedisConf {

    private Optional<String> masterName;//哨兵模式专用

    private Optional<String> nodes;//redis链接信息

    private Optional<String> pwd;

    private Optional<String> host;//为做单节点的兼容

    private Optional<String> port;//为做单节点的兼容

    private Optional<Integer> maxActive = Optional.of(100);//最大连接数, 默认100个

    private Optional<Integer> maxIdle = Optional.of(100); //最大空闲连接数, 默认100个

    private Optional<Integer> minIdle = Optional.of(10);//最小空闲连接数, 默认100个

    private Optional<Integer> maxWait = Optional.of(1000) ; //获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1

    private boolean isSentinel = false; //是否时哨兵模式

    private RedisMode redisMode = RedisMode.single; //默认单节点

    public static JedisConf init() {
        ConfigTemplate configTemplate  = ConfigTemplate.buildDefaultConfigTemplate();
        JedisConf jedisConf = new JedisConf();
        jedisConf.SetMasterName(configTemplate.getPropertyValue("spring.redis.masterName", defaultValue(String.class)));
        jedisConf.nodes = Optional.of(configTemplate.getPropertyValue("spring.redis.nodes",defaultValue(String.class)));
        jedisConf.pwd = Optional.of(configTemplate.getPropertyValue("spring.redis.pwd",defaultValue(String.class)));
        jedisConf.host = Optional.of(configTemplate.getPropertyValue("spring.redis.host",defaultValue(String.class)));
        jedisConf.port = Optional.of(configTemplate.getPropertyValue("spring.redis.port","6379"));
        jedisConf.maxWait = Optional.of(configTemplate.getPropertyAsNumber("redis.maxWait", 10000,Integer.class));
        jedisConf.maxActive = Optional.of(configTemplate.getPropertyAsNumber("redis.maxActive", 100,Integer.class));
        jedisConf.maxIdle = Optional.of(configTemplate.getPropertyAsNumber("redis.maxIdle", 100,Integer.class));
        jedisConf.minIdle = Optional.of(configTemplate.getPropertyAsNumber("redis.minIdle", 10,Integer.class));
        return jedisConf;
    }

    public static JedisConf buildSimple(String host, String port) {
        checkNotNull(host);
        checkNotNull(port);
        return buildSimple(host,port,null);
    }

    public static JedisConf buildSimple(String host, String port, String pwd) {
        checkNotNull(host);
        checkNotNull(port);
        JedisConf jedisConf = new JedisConf();
        jedisConf.nodes = Optional.of(String.format("%s:%s",host,port));
        jedisConf.pwd = Optional.of(pwd);
        jedisConf.host = Optional.of(host);
        jedisConf.port = Optional.of(port);
        return jedisConf;
    }


    public static JedisConf buildSentinel(String master, String nodes, String pwd) {
        checkNotNull(master);
        checkNotNull(nodes);
        JedisConf jedisConf = new JedisConf();
        jedisConf.SetMasterName(master);
        jedisConf.nodes = Optional.of(nodes);
        jedisConf.pwd = Optional.of(pwd);
        return jedisConf;
    }


    public void SetMasterName(String master) {
        this.masterName = Optional.of(master);
        //当masterName不为空时，默认为哨兵模式
        if (!isNullOrEmpty(this.masterName.orElseGet(()->null))) {
            isSentinel = true;
            redisMode = RedisMode.sentinel;
        }
    }

    public String getUniqueId() {
        StringBuilder stringBuilder = new StringBuilder();
        List<Field> fields = ReflectionUtils.getFields(this.getClass());
        for (Field field:fields) {
            Object value = ReflectionUtils.getFiledValue(field, this);
            if (value != null) {
                stringBuilder.append(value instanceof Optional? ((Optional) value).get(): value);
            }
        }
        checkArgument(stringBuilder.length() > 0, "错误的对象信息");
        return StringUtils.convertToMD5(stringBuilder.toString());
    }

}
