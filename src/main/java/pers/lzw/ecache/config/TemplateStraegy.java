package pers.lzw.ecache.config;

import pers.lzw.ecache.config.context.StringTypeContext;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 策略类，用于生成调用生成想要的链接体
 * @author liu
 *
 * @param 
 */
public class TemplateStraegy {
	
	private TemplateSource source;

	public TemplateStraegy (TemplateSource baseTemplate) {
		source = checkNotNull(baseTemplate);
	}
	
	public StringTypeContext getInstance() {
		return source.getInstance();
	}
	
	public StringTypeContext getInstance(String path) {
		return source.getInstance(path);
	}
}
