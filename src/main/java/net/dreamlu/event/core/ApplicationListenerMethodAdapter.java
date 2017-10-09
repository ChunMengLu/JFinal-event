package net.dreamlu.event.core;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class ApplicationListenerMethodAdapter implements ApplicationListener<ApplicationEvent> {
	private final Method method;

	private final Class<?> targetClass;

	private final List<Class<?>> declaredEventClasses;

	private final String condition;

	private final int order;

	private final AnnotatedElementKey methodKey;
	
	public ApplicationListenerMethodAdapter(Class<?> targetClass, Method method) {
		this.method = method;
		this.targetClass = targetClass;
		
		EventListener ann = method.getAnnotation(EventListener.class);
		this.declaredEventClasses = Arrays.asList(ann.classes());
		this.condition = (ann != null ? ann.condition() : null);
		this.order = ann.order();
		
		this.methodKey = new AnnotatedElementKey(method, targetClass);
	}

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
//		this.method.invoke(obj, event);
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
}
