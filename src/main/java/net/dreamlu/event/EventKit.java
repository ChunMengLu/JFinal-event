package net.dreamlu.event;

import java.util.*;
import java.util.concurrent.ExecutorService;

import com.jfinal.log.Log;

import net.dreamlu.event.core.ApplicationEvent;
import net.dreamlu.event.core.ApplicationListenerMethodAdapter;
import net.dreamlu.event.core.EventType;
import net.dreamlu.utils.ConcurrentMultiMap;

/**
 * 事件工具类
 * @author L.cm
 * email: 596392912@qq.com
 * site:http://www.dreamlu.net
 * date 2015年4月26日下午9:58:53
 */
public class EventKit {
	private static Log log = Log.getLog(EventKit.class);

	private static List<ApplicationListenerMethodAdapter> listeners;
	private static ExecutorService pool;
	/**
	 * 缓存提高性能
	 */
	static ConcurrentMultiMap<EventType, ApplicationListenerMethodAdapter> cache
			= new ConcurrentMultiMap<>();

	static void init(List<ApplicationListenerMethodAdapter> listeners, ExecutorService pool) {
		EventKit.listeners = listeners;
		EventKit.pool = pool;
	}

	/**
	 * 获取监听器
	 */
	private static List<ApplicationListenerMethodAdapter> getListener(EventType eventType) {
		Objects.requireNonNull(listeners, "listeners is null, 请先初始化EventPlugin");
		if (listeners.isEmpty()) {
			log.error("EventListener is empty!");
			return Collections.emptyList();
		}
		return cache.computeIfAbsent(eventType, (key) -> initListeners(listeners, key));
	}

	/**
	 * 初始化监听器
	 */
	private static List<ApplicationListenerMethodAdapter> initListeners(List<ApplicationListenerMethodAdapter> listeners, EventType eventType) {
		final Class<?> eventClass = eventType.getEventClass();
		final Class<?> sourceClass = eventType.getSourceClass();
		final Class<?> sourceEventClass = sourceClass == null ? eventClass : sourceClass;
		final List<ApplicationListenerMethodAdapter> list = new ArrayList<>();
		for (ApplicationListenerMethodAdapter listener : listeners) {
			// 1. 注解上的事件类型
			List<Class<?>> declaredEventClasses = listener.getDeclaredEventClasses();
			if (!declaredEventClasses.isEmpty()) {
				boolean canExec = false;
				for (Class<?> annType : declaredEventClasses) {
					// 2.判断注解类型
					if (sourceEventClass.isAssignableFrom(annType)) {
						canExec = true;
						break;
					}
				}
				if (!canExec) {
					continue;
				}
			}
			// 2. 判断方法参数类型
			if (listener.getParamCount() > 0) {
				// 方法参数事件类型
				Class<?> paramType = listener.getParamType();
				// 参数类型不支持跳出
				if (!paramType.isAssignableFrom(sourceEventClass)) {
					continue;
				}
			}
			list.add(listener);
		}
		// 对兼听器排序
		if (list.size() > 1) {
			list.sort(Comparator.comparingInt(ApplicationListenerMethodAdapter::getOrder));
		}
		return list;
	}


	/**
	 * 发布事件
	 * @param event Object
	 */
	public static void post(Object event) {
		Objects.requireNonNull(event, "EventKit post event 不能为null");
		Class<?> eventClass = event.getClass();
		EventType eventType;
		if (event instanceof ApplicationEvent){
			eventType = new EventType(eventClass, null);
		} else {
			eventType = new EventType(ApplicationEvent.class, event.getClass());
		}
		post(event, eventType);
	}

	/**
	 * 发布事件
	 * @param event Object
	 * @param eventType SourceEventType
	 */
	private static void post(final Object event, EventType eventType) {
		final List<ApplicationListenerMethodAdapter> listenerList = getListener(eventType);
		for (final ApplicationListenerMethodAdapter listener : listenerList) {
			if (null != pool && listener.isAsync()) {
				pool.submit(() -> listener.onApplicationEvent(event));
			} else {
				listener.onApplicationEvent(event);
			}
		}
	}

}
