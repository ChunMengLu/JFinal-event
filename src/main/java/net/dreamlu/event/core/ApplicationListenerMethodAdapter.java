package net.dreamlu.event.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import com.jfinal.kit.ElKit;
import com.jfinal.kit.Kv;
import com.jfinal.kit.StrKit;

import net.dreamlu.utils.BeanUtil;

public class ApplicationListenerMethodAdapter implements ApplicationListener<ApplicationEvent> {
	private final Method method;

	private final Class<?> targetClass;

	private final List<Class<?>> declaredEventClasses;

	private final String condition;

	private final int order;

	private final boolean async;

	private final AnnotatedElementKey methodKey;
	
	public ApplicationListenerMethodAdapter(Class<?> targetClass, Method method) {
		this.method = method;
		this.targetClass = targetClass;
		
		EventListener ann = method.getAnnotation(EventListener.class);
		this.declaredEventClasses = Arrays.asList(ann.events());
		this.condition = (ann != null ? ann.condition() : null);
		this.order = ann.order();
		this.async = ann.async();
		
		this.methodKey = new AnnotatedElementKey(method, targetClass);
	}

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		String condition = this.getCondition();
		// 判断表达式
		if (StrKit.notBlank(condition)) {
			boolean elPass = ElKit.eval(condition, Kv.by("event", event));
			if (!elPass) {
				return;
			}
		}
		// 用户事件类型限制
		Class<?> paramType = method.getParameterTypes()[0];
		if (!declaredEventClasses.isEmpty()) {
			boolean canExec = false;
			for (Class<?> clazz : declaredEventClasses) {
				if (paramType.isAssignableFrom(clazz)) {
					canExec = true;
					break;
				}
			}
			if (!canExec) return;
		}
		// 参数支持的事件类型
		Class<?> eventType = event.getClass();
		if (!paramType.isAssignableFrom(eventType)) {
			return;
		}
		try {
			this.method.invoke(BeanUtil.newInstance(targetClass), event);
		} catch (IllegalAccessException 
				| IllegalArgumentException
				| InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public Method getMethod() {
		return method;
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

	public AnnotatedElementKey getMethodKey() {
		return methodKey;
	}

	@Override
	public String toString() {
		return "ApplicationListenerMethodAdapter [targetClass=" + targetClass
		        + ", method=" + method + "]";
	}

}
