package net.dreamlu.processor;

import com.jfinal.log.Log;
import net.dreamlu.event.EventPlugin;
import net.dreamlu.event.core.EventListener;

import javax.annotation.Nullable;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * 事件加载器
 *
 * @author L.cm
 * email: 596392912@qq.com
 * site:http://www.dreamlu.net
 * date 2019年7月17日下午9:36:17
 */
public final class DreamEventsLoader {
	/**
	 * The package separator character: {@code '.'}.
	 */
	private static final char PACKAGE_SEPARATOR = '.';
	/**
	 * The inner class separator character: {@code '$'}.
	 */
	private static final char INNER_CLASS_SEPARATOR = '$';
	/**
	 * The location to look for dream.events file
	 * <p>Can be present in multiple JAR files.
	 */
	public static final String DREAM_EVENTS_RESOURCE_LOCATION = "META-INF/dream.events";
	private static final Log logger = Log.getLog(DreamEventsLoader.class);

	/**
	 * 加载所以事件的方法
	 *
	 * @param registeredClass 手动注册的类
	 * @return 方法集合
	 */
	public static Set<Method> loadEventMethods(Set<Class<?>> registeredClass) {
		ClassLoader classLoader = EventPlugin.class.getClassLoader();
		Set<Class<?>> eventClassSet = loadEventClass(classLoader);
		eventClassSet.addAll(registeredClass);
		Set<Method> methodSet = new HashSet<>();
		for (Class<?> eventClass : eventClassSet) {
			Method[] methods = eventClass.getMethods();
			for (Method method : methods) {
				Annotation ann = method.getAnnotation(EventListener.class);
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
		}
		return methodSet;
	}

	/**
	 * 加载组件类列表
	 *
	 * @param classLoader ClassLoader
	 * @return 类列表
	 */
	private static Set<Class<?>> loadEventClass(@Nullable ClassLoader classLoader) {
		Set<Class<?>> result = new HashSet<>();
		try {
			Enumeration<URL> urls = (classLoader != null ?
				classLoader.getResources(DREAM_EVENTS_RESOURCE_LOCATION) :
				ClassLoader.getSystemResources(DREAM_EVENTS_RESOURCE_LOCATION));
			while (urls.hasMoreElements()) {
				URL url = urls.nextElement();
				Properties properties = new Properties();
				properties.load(url.openStream());
				for (Map.Entry<?, ?> entry : properties.entrySet()) {
					String eventClazz = ((String) entry.getKey()).trim();
					if (logger.isDebugEnabled()) {
						logger.debug("Loaded event class [" + eventClazz + "]");
					}
					result.add(loadClazz(eventClazz, classLoader));
				}
			}
			return result;
		} catch (IOException ex) {
			throw new IllegalArgumentException("Unable to load factories from location [" + DREAM_EVENTS_RESOURCE_LOCATION + "]", ex);
		}
	}

	private static Class<?> loadClazz(String name, ClassLoader clToUse) {
		try {
			return Class.forName(name, false, clToUse);
		} catch (ClassNotFoundException ex) {
			int lastDotIndex = name.lastIndexOf(PACKAGE_SEPARATOR);
			if (lastDotIndex != -1) {
				String innerClassName =
					name.substring(0, lastDotIndex) + INNER_CLASS_SEPARATOR + name.substring(lastDotIndex + 1);
				try {
					return Class.forName(innerClassName, false, clToUse);
				} catch (ClassNotFoundException ex2) {
					// Swallow - let original exception get through
				}
			}
			throw new IllegalArgumentException("Unable to load class [" + name + "]", ex);
		}
	}

}
