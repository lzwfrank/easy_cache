package pers.lzw.ecache.base;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import pers.lzw.ecache.config.impl.EnvironmentConfigSource;

/**
 * 如果使用spring 优先把EnvironmentConfigSource注入进bean工厂中
 * *@author:wen.wen
 * *@create 2020-11-03 16:17
 **/
@Component
@ConditionalOnProperty(value = "custom.config.classpath",havingValue = "com.trs.tcache.conf.EnvironmentConfigSource")
public class ConfigLoadPostProcess implements BeanFactoryAware, InstantiationAwareBeanPostProcessor {

    private ConfigurableListableBeanFactory beanFactory;

    private static boolean loadFlag = false;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableListableBeanFactory)beanFactory;
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        if (!loadFlag) {
            synchronized (this) {
                if (!loadFlag) {
                    loadFlag = true;
                    beanFactory.getBean(EnvironmentConfigSource.class);
                }
            }
        }
        return null;
    }
}
