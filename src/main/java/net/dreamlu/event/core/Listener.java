package net.dreamlu.event.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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

	// The order value. Default is {@link Integer#MAX_VALUE}.
	int order() default Integer.MAX_VALUE;

	// 标记Listener是否为异步，Default is false
	boolean enableAsync() default false;

}