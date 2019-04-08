package net.dreamlu.event;

import net.dreamlu.utils.ClassUtil.ClassFilter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * 类方法查找器
 *
 * @author L.cm
 * email: 596392912@qq.com
 * site:http://www.dreamlu.net
 * date 2017年10月10日上午11:27:24
 */
class MethodEventFilter implements ClassFilter {
	private final Class<? extends Annotation> annotationClass;
	private final Set<Method> methodSet;

	MethodEventFilter(Class<? extends Annotation> annotationClass) {
		this.annotationClass = annotationClass;
		this.methodSet = new HashSet<Method>();
	}

	@Override
	public boolean accept(Class<?> clazz) {
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			Annotation ann = method.getAnnotation(annotationClass);
			if (ann == null) {
				continue;
			}
			// 支持参数个数为 0 或者 1
			int parameterCount = method.getParameterCount();
			if (parameterCount > 1) {
				continue;
			}
			methodSet.add(method);
		}
		return false;
	}

	@Override
	public void addClass(Class<?> clazz) {
	}

	Set<Method> getListeners() {
		return methodSet;
	}

	void filter(Set<Class<?>> registeredClass) {
		for (Class<?> clazz : registeredClass) {
			accept(clazz);
		}
	}
}
