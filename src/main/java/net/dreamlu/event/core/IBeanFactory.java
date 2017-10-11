package net.dreamlu.event.core;

/**
 * Bean工厂
 * @author L.cm
 * email: 596392912@qq.com
 * site:http://www.dreamlu.net
 * date 2017年10月11日下午9:58:53
 */
public interface IBeanFactory {
	<T> T getBean(Class<T> requiredType) throws Exception;
}