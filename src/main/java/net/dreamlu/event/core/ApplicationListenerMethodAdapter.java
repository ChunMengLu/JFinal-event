package net.dreamlu.event.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import com.jfinal.kit.ElKit;
import com.jfinal.kit.Kv;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;

/**
 * 监听器封装
 * @author L.cm
 * email: 596392912@qq.com
 * site:http://www.dreamlu.net
 * date 2017年10月10日上午11:27:24
 */
public class ApplicationListenerMethodAdapter implements ApplicationListener<ApplicationEvent> {
	private static Log log = Log.getLog(ApplicationListenerMethodAdapter.class);
	private final IBeanFactory beanFactory;

	private final Method method;

	private final Class<?> paramType;

	private final Class<?> targetClass;

	private final List<Class<?>> declaredEventClasses;

	private final String condition;

	private final int order;

	private final boolean async;

	public ApplicationListenerMethodAdapter(IBeanFactory beanFactory, Class<?> targetClass, Method method) {
		this.beanFactory = beanFactory;
		this.method = method;
		this.paramType = method.getParameterTypes()[0];
		this.targetClass = targetClass;
		// 事件注解上的信息
		EventListener ann = method.getAnnotation(EventListener.class);
		this.declaredEventClasses = Arrays.asList(ann.events());
		this.condition = (ann != null ? ann.condition() : null);
		this.order = ann.order();
		this.async = ann.async();
	}

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		// 判断表达式
		if (StrKit.notBlank(this.condition)) {
			boolean elPass = ElKit.eval(this.condition, Kv.by("event", event));
			log.debug("method:[" + this.method + "]-condition:[" + this.condition + "]-result:[" + elPass + "]");
			if (!elPass) {
				return;
			}
		}
		try {
			Object bean = beanFactory.getBean(this.targetClass);
			this.method.invoke(bean, event);
		} catch (Exception e) {
			handleException(e);
		}
	}

	/**
	 * 将异常抛出更加合理
	 */
	private void handleException(Exception e) {
		if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException
				|| e instanceof NoSuchMethodException) {
			throw new IllegalArgumentException(e);
		} else if (e instanceof InvocationTargetException) {
			throw new RuntimeException(((InvocationTargetException) e).getTargetException());
		} else if (e instanceof RuntimeException) {
			throw (RuntimeException) e;
		} else {
			throw new RuntimeException(e);
		}
	}

	public Method getMethod() {
		return method;
	}

	public Class<?> getParamType() {
		return paramType;
	}

	public Class<?> getTargetClass() {
		return targetClass;
	}

	public List<Class<?>> getDeclaredEventClasses() {
		return declaredEventClasses;
	}

	public String getCondition() {
		return condition;
	}

	public int getOrder() {
		return order;
	}

	public boolean isAsync() {
		return async;
	}

	@Override
	public String toString() {
		return "@EventListener [" + method + "]";
	}

}
