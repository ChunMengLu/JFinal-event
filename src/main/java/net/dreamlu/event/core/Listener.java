package net.dreamlu.event.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.dreamlu.event.EventType;

/**
 * 注解标记需要扫描的监听器
 * @author L.cm
 * email: 596392912@qq.com
 * site:http://www.dreamlu.net
 * date 2015年6月24日下午11:27:24
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Listener {

	/**
	 * The order value. Default is {@link Integer#MAX_VALUE}.
	 * @return order
	 */
	int order() default Integer.MAX_VALUE;

	/**
	 * 标记Listener是否为异步，Default is false
	 * @return async
	 */
	boolean enableAsync() default false;

	/**
	 * 事件的tag，事件的标识符 by L.cm 2016-08-19
	 * @since 1.4.0
	 * @return tag
	 */
	String tag() default EventType.DEFAULT_TAG;
}