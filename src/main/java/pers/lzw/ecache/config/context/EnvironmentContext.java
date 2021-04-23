package pers.lzw.ecache.config.context;

import org.springframework.core.env.Environment;
import pers.lzw.ecache.config.context.StringTypeContext;

/**
 * @author wenwen
 * Created on 2019/10/14
 */
public class EnvironmentContext extends StringTypeContext {

    Environment environment;

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public <T extends Number> T getPropertyAsNumber(String property, Class<T> classType, T defaultValue) {
        return environment.getProperty(property, classType, defaultValue);
    }

    @Override
    public String getProperty(String property) {
        return environment.getProperty(property);
    }

    @Override
    public String getProperty(String property, String defaultValue) {
        return environment.getProperty(property, defaultValue);
    }

    @Override
    public boolean containProperty(String property) {
        return environment.containsProperty(property);
    }

    @Override
    @Deprecated
    public Object getPropertyAsObject(String property) {
        return environment.getProperty(property);
    }

    @Override
    @Deprecated
    public Object getPropertyAsObject(String property, Object defaultObj) {
        return null;
    }

    @Override
    @Deprecated
    public void addProperty(String propertyName, Object propertyValue) {
    }

    @Override
    @Deprecated
    public void addProperty(Object propertyName, Object propertyValue, Object... options) {
    }
}
