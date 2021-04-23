package pers.lzw.ecache.config;

import pers.lzw.ecache.config.context.StringTypeContext;
import pers.lzw.ecache.config.impl.PropertiesSource;

import static com.google.common.base.Defaults.defaultValue;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;

public class ConfigTemplate {

    private final static String OPEN_DEFAULT = "1";

    private final static String STRING_DEFAULT = defaultValue(String.class);

    private final static String CUSTOM_CONFIG_CLASSPATH = "custom.config.classpath";

    //默认读取配置文件
    private StringTypeContext context = null;

    private ConfigTemplate() {
    }

    /**
     * 选择读取模式
     *
     * @param source 实现TemplateSource接口的具体实现类
     */
    public static ConfigTemplate build(TemplateSource source) {
        ConfigTemplate configTemplate = new ConfigTemplate();
        configTemplate.context = new TemplateStraegy(source).getInstance();
        return configTemplate;
    }

    public static ConfigTemplate build(TemplateSource source, String path) {
        ConfigTemplate configTemplate = new ConfigTemplate();
        configTemplate.context = new TemplateStraegy(source).getInstance(path);
        return configTemplate;
    }

    /**
     * 构建一个默认的ConfigTemplate
     * 通过对配置文件扫描的方式来判断生成PropertiesSource还是ConfigurationCenterSource(配置中心方式获取配置)
     * 只有当配置文件中存在configuration.center.enable且值为true时，才会生成ConfigurationCenterSource
     *
     * @return3
     */
    public static ConfigTemplate buildDefaultConfigTemplate() {
        ConfigTemplate configTemplate = new ConfigTemplate();
        try {
            //默认是PropertiesSource形式
            configTemplate.context = new TemplateStraegy(new PropertiesSource()).getInstance();
            //如果有nacos相关配置则使用nacos
            String className = configTemplate.getPropertyValue(CUSTOM_CONFIG_CLASSPATH);
            if (!isNullOrEmpty(className)) {
                TemplateSource templateSource = (TemplateSource) Class.forName(className).getConstructor().newInstance();
                configTemplate.context = new TemplateStraegy(templateSource).getInstance();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return configTemplate;
    }

    public <T extends Number> T getPropertyAsNumber(String key,T defaultValue, Class<T> clazz) {
        checkNotNull(key);
        checkNotNull(defaultValue);
        T value = context.getPropertyAsNumber(key, clazz, defaultValue);
        return value;
    }

    /**
     * 获取配置项的值
     *
     * @param key
     * @return
     */
    public String getPropertyValue(String key, String defaultValue) {
        return context.containProperty(checkNotNull(key, "the key must not be null")) ?
                context.getProperty(key) :
                defaultValue;
    }

    /**
     * 添加配置项
     *
     * @param key
     * @param value
     * @return
     */
    public void addPropertyValue(String key, String value) {
        context.addProperty(key, value);
    }

    /**
     * 获取配置项的值
     *
     * @param key
     * @return
     */
    public String getPropertyValue(String key) {
        return getPropertyValue(key, STRING_DEFAULT);
    }

    /**
     * 判断配置项所对应功能是否开启
     *
     * @param key 配置项
     * @return
     */
    public boolean isOpen(String key) {
        return isEqual(key, OPEN_DEFAULT);
    }

    /**
     * 判断配置项所对应的值是否与传入值相等
     *
     * @param key        配置项
     * @param judgeValue 传入值
     * @return
     */
    public boolean isEqual(String key, String judgeValue) {
        checkNotNull(key, "the key must not be null");
        checkNotNull(judgeValue, "the judgeValue must not be null");
        String flag = context.getProperty(key);
        return judgeValue.equals(flag.trim()) ? true : false;
    }
}
