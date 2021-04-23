package pers.lzw.ecache.config.impl;

import pers.lzw.ecache.config.TemplateSource;
import pers.lzw.ecache.config.context.CommonContext;
import pers.lzw.ecache.config.context.StringTypeContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * 2016年8月26日
 * @author liu
 *
 */
public class PropertiesSource implements TemplateSource {

    private static Map<String, CommonContext> readedPropertiesValue = new ConcurrentHashMap<>();
	
	private static final Object KEY = new Object();
	
	private static final String PROPERTIES_PATH = "/application.properties";
	
	@Override
	public StringTypeContext getInstance() {
		return getInstance(PROPERTIES_PATH);
	}
	
	@Override
	public StringTypeContext getInstance( String path) {
		checkNotNull(path);
		if(!readedPropertiesValue.containsKey(path)){
            synchronized (KEY) {
            	if (!readedPropertiesValue.containsKey(path))
            		init(path);
            }
        }
		return readedPropertiesValue.get(path);
	}  
	
	/**
	 * 初始化commonContext
	 * 读取配置文件信息存入内存
	 */
	@SuppressWarnings("unchecked")
	private static void init(String path) {
		checkArgument(!isNullOrEmpty(path), "properties can't be null");
		Properties properties = new Properties();
		CommonContext context = new CommonContext(); //初始化commonContext
		try (InputStream in = PropertiesSource.class.getResourceAsStream(path);){
			properties.load(in);
			//获取配置项名的列表
			Enumeration<String> propertyNames = (Enumeration<String>) properties.propertyNames();
			while (propertyNames.hasMoreElements()) {
				String nextElement = propertyNames.nextElement();
				//只有当配置项找不到时才会为null，所以此处无需非空判断
				context.addProperty(nextElement, properties.getProperty(nextElement));
			}
			readedPropertiesValue.put(path, context);	
		} catch (IOException e) {
			e.printStackTrace();
		}
   }
}
