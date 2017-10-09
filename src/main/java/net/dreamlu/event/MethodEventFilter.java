package net.dreamlu.event;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.dreamlu.event.core.ApplicationEvent;
import net.dreamlu.event.core.ApplicationListenerMethodAdapter;
import net.dreamlu.utils.ClassUtil.ClassFilter;

public class MethodEventFilter implements ClassFilter {
	
	private final Class<? extends Annotation> annotationClass;
	private final List<ApplicationListenerMethodAdapter> listenerList;
	
	public MethodEventFilter(Class<? extends Annotation> annotationClass) {
		this.annotationClass = annotationClass;
		this.listenerList = new ArrayList<ApplicationListenerMethodAdapter>();
	}

	@Override
	public boolean accept(Class<?> clazz) {
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			Annotation ann = method.getAnnotation(annotationClass);
			if (ann == null) continue;
			Class<?>[] classx = method.getParameterTypes();
			if (classx.length != 1) continue;
			Class<?> eventClass = classx[0];
			if (ApplicationEvent.class.isAssignableFrom(eventClass)) {
				listenerList.add(new ApplicationListenerMethodAdapter(clazz, method));
			}
		}
		return false;
	}

	@Override
	public void addClass(Class<?> clazz) {}

	public List<ApplicationListenerMethodAdapter> getListenerList() {
		return listenerList;
	}
}
