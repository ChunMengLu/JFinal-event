package net.dreamlu.event;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

import com.jfinal.log.Log;

import net.dreamlu.event.core.ApplicationEvent;
import net.dreamlu.event.core.ApplicationListener;
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
	private static ConcurrentMultiMap cache = new ConcurrentMultiMap<>();

	static void init(List<ApplicationListenerMethodAdapter> listeners, ExecutorService pool) {
		EventKit.listeners = listeners;
		EventKit.pool = pool;
	}
	
	private static List<ApplicationListenerMethodAdapter> getListener(final ApplicationEvent event) {
		Objects.requireNonNull(listeners, "请先初始化EventPlugin");
		if (listeners.isEmpty()) {
			log.error("EventListener is empty!");
			return Collections.emptyList();
		}
		
		
	}
	
	/**
	 * 发布事件
	 * @param event ApplicationEvent
	 * @since 2.0.0
	 */
	public static void post(final ApplicationEvent event) {
		EventKit.eventActuator(new EventCall() {
			@Override
			public void exec(ApplicationListener listener) {
				listener.onApplicationEvent(event);
			}
		});
	}
	
	public interface EventCall {
		void exec(ApplicationListener listener);
	}
	
	/**
	 * 事件执行方法
	 * @param eventType 事件类型
	 * @param call EventCall
	 * @return 
	 */
	private static void eventActuator(final EventCall call) {
//		Collection<ListenerHelper> listenerList = map.get(eventType);
//		for (final ListenerHelper helper : listenerList) {
//			if (null != pool && helper.enableAsync) {
//				pool.submit(new Runnable() {
//					@Override
//					public void run() {
//						call.exec(helper.listener);
//					}
//				});
//			} else {
//				 call.exec(helper.listener);
//			}
//		}
	}

}
