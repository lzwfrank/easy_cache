package pers.lzw.ecache.config;

import pers.lzw.ecache.config.context.StringTypeContext;

/**
 * 2016年8月26日
 * @author liu
 *
 */
public interface TemplateSource {

	StringTypeContext getInstance();

	StringTypeContext getInstance(String path);

}
