package pers.lzw.ecache.config.impl;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import pers.lzw.ecache.config.context.EnvironmentContext;
import pers.lzw.ecache.config.TemplateSource;
import pers.lzw.ecache.config.context.StringTypeContext;

import javax.annotation.PostConstruct;

/**
 * @author wenwen
 * Created on 2019/9/17
 */
@Component
@ConditionalOnProperty(value = "custom.config.classpath",havingValue = "com.trs.tcache.conf.EnvironmentConfigSource")
public class EnvironmentConfigSource implements TemplateSource, EnvironmentAware {

    private static final Object KEY = new Object();

    private static final String PROPERTIES_PATH = "environment";

    private static EnvironmentContext environmentContext;

    private static Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        EnvironmentConfigSource.environment = environment;
    }

    @PostConstruct
    public void initParams() {
        this.getInstance(PROPERTIES_PATH);
        environmentContext.setEnvironment(environment);
    }

    @Override
    public StringTypeContext getInstance() {
        return getInstance(PROPERTIES_PATH);
    }

    @Override
    public StringTypeContext getInstance(String path) {
        if (environmentContext == null) {
            synchronized (KEY) {
                if (environmentContext == null) {
                    environmentContext = new EnvironmentContext();
                }
            }
        }
        return environmentContext;
    }
}
