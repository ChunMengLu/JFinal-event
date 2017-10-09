package net.dreamlu.event.core;

import java.lang.annotation.Documented;
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
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface EventListener {

	/**
	 * The event classes that this listener handles.
	 * <p>If this attribute is specified with a single value, the
	 * annotated method may optionally accept a single parameter.
	 * However, if this attribute is specified with multiple values,
	 * the annotated method must <em>not</em> declare any parameters.
	 */
	Class<?>[] events() default {};
	
	/**
	 * The order value. Default is {@link Integer#MAX_VALUE}.
	 * @return order
	 */
	int order() default Integer.MAX_VALUE;

	/**
	 * 标记Listener是否为异步，Default is false
	 * @return async
	 */
	boolean async() default false;

	/**
	 * JFinal el
	 */
	String condition() default "";

}