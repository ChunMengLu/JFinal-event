package net.dreamlu.event;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import net.dreamlu.event.core.ApplicationEvent;
import net.dreamlu.utils.ClassUtil.ClassFilter;

/**
 * 类方法查找器
 * @author L.cm
 * email: 596392912@qq.com
 * site:http://www.dreamlu.net
 * date 2017年10月10日上午11:27:24
 */
class MethodEventFilter implements ClassFilter {
	private final Class<? extends Annotation> annotationClass;
	private final Set<Method> methodSet;
	
	public MethodEventFilter(Class<? extends Annotation> annotationClass) {
		this.annotationClass = annotationClass;
		this.methodSet = new HashSet<Method>();
	}

	@Override
	public boolean accept(Class<?> clazz) {
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			Annotation ann = method.getAnnotation(annotationClass);
			if (ann == null) continue;
			Class<?>[] paramTypes = method.getParameterTypes();
			if (paramTypes.length != 1) continue;
			Class<?> eventType = paramTypes[0];
			if (ApplicationEvent.class.isAssignableFrom(eventType)) {
				methodSet.add(method);
			}
		}
		return false;
	}

	@Override
	public void addClass(Class<?> clazz) {}

	public Set<Method> getListeners() {
		return methodSet;
	}

	public void filter(Set<Class<?>> registeredClass) {
		for (Class<?> clazz : registeredClass) {
			accept(clazz);
		}
	}
}