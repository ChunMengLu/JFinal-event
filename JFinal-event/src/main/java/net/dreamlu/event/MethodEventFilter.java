package net.dreamlu.event;

import net.dreamlu.event.core.EventListener;
import net.dreamlu.utils.ClassUtil.ClassFilter;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedHashSet;
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
	private final Class<EventListener> annotationClass;
	private final Set<Method> methodSet;

	MethodEventFilter(Class<EventListener> annotationClass) {
		this.annotationClass = annotationClass;
		this.methodSet = new LinkedHashSet<>();
	}

	@Override
	public boolean accept(Class<?> clazz) {
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			EventListener listener = method.getAnnotation(annotationClass);
			if (listener == null) {
				continue;
			}
			// 支持参数个数为 0 或者 1
			int parameterCount = method.getParameterCount();
			if (parameterCount > 1) {
				continue;
			}
			// 仅仅支持 pub 非 static 的方法
			int mod = method.getModifiers();
			if (Modifier.isPublic(mod) && !Modifier.isStatic(mod)) {
				methodSet.add(method);
			}
		}
		return false;
	}

	@Override
	public void addClass(Class<?> clazz) {
	}

	Set<Method> getListeners() {
		return methodSet;
	}

}
