package net.dreamlu.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;

import com.jfinal.log.Log;

import net.dreamlu.event.core.ApplicationEvent;
import net.dreamlu.event.core.ApplicationListenerMethodAdapter;
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
	// 缓存提高性能
	protected static ConcurrentMultiMap<Class<?>, ApplicationListenerMethodAdapter> cache 
			= new ConcurrentMultiMap<Class<?>, ApplicationListenerMethodAdapter>();
	private static Object locker = new Object();
	
	static void init(List<ApplicationListenerMethodAdapter> listeners, ExecutorService pool) {
		EventKit.listeners = listeners;
		EventKit.pool = pool;
	}
	
	/**
	 * 获取监听器
	 */
	private static List<ApplicationListenerMethodAdapter> getListener(final ApplicationEvent<?> event) {
		if (listeners == null) {
			log.error("listeners is null, 请先初始化EventPlugin");
			throw new NullPointerException("请先初始化EventPlugin");
		}
		if (listeners.isEmpty()) {
			log.error("EventListener is empty!");
			return Collections.emptyList();
		}
		// 事件类
		Class<?> eventType = event.getClass();
		List<ApplicationListenerMethodAdapter> _listeners = cache.get(eventType);
		if (_listeners == null) {
			synchronized (locker) {
				if (_listeners == null) {
					_listeners = initListeners(listeners, eventType);
					cache.putAll(eventType, _listeners);
				}
			}
		}
		return _listeners;
	}
	
	/**
	 * 初始化监听器
	 */
	private static List<ApplicationListenerMethodAdapter> initListeners(List<ApplicationListenerMethodAdapter> listeners, Class<?> eventType) {
		final List<ApplicationListenerMethodAdapter> list = new ArrayList<ApplicationListenerMethodAdapter>();
		for (ApplicationListenerMethodAdapter listener : listeners) {
			// 方法参数事件类型
			Class<?> paramType = listener.getParamType();
			// 1.判断参数类型
			if (!paramType.isAssignableFrom(eventType)) {
				continue;
			}
			// 注解上的事件类型
			List<Class<?>> declaredEventClasses = listener.getDeclaredEventClasses();
			if (!declaredEventClasses.isEmpty()) {
				boolean canExec = false;
				for (Class<?> annType : declaredEventClasses) {
					// 2.判断注解类型
					if (eventType.isAssignableFrom(annType)) {
						canExec = true;
						break;
					}
				}
				if (!canExec) {
					continue;
				}
			}
			list.add(listener);
		}
		// 对兼听器排序
		if (list.size() > 1) {
			Collections.sort(list, new Comparator<ApplicationListenerMethodAdapter>() {
				@Override
				public int compare(ApplicationListenerMethodAdapter o1, ApplicationListenerMethodAdapter o2) {
					int x = o1.getOrder(); int y = o2.getOrder();
					return (x < y) ? -1 : ((x == y) ? 0 : 1);
				}
			});
		}
		return list;
	}
	
	/**
	 * 发布事件
	 * @param event ApplicationEvent
	 */
	public static void post(final ApplicationEvent<?> event) {
		final List<ApplicationListenerMethodAdapter> _listeners = getListener(event);
		for (final ApplicationListenerMethodAdapter listener : _listeners) {
			if (null != pool && listener.isAsync()) {
				pool.submit(new Runnable() {
					@Override
					public void run() {
						listener.onApplicationEvent(event);
					}
				});
			} else {
				listener.onApplicationEvent(event);
			}
		}
	}

}
