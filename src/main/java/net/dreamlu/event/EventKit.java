package net.dreamlu.event;

import net.dreamlu.event.core.ApplicationEvent;


/**
 * 事件工具类
 * @author L.cm
 * email: 596392912@qq.com
 * site:http://www.dreamlu.net
 * date 2015年4月26日下午9:58:53
 */
@SuppressWarnings("rawtypes")
public class EventKit {
//	private static ArrayListMultimap<EventType, ListenerHelper> map;
//	private static ExecutorService pool;
//
//	static void init(ArrayListMultimap<EventType, ListenerHelper> map, ExecutorService pool) {
//		EventKit.map = map;
//		EventKit.pool = pool;
//	}
//	
	/**
	 * 发布事件
	 * @param event ApplicationEvent
	 * @since 1.4.0
	 */
	public static void post(final ApplicationEvent event) {
//		post(EventType.DEFAULT_TAG, event);
	}
	
	/**
	 * 发布事件
	 * @param tag 标记
	 * @param event 事件
	 * @since 1.4.0
	 */
	public static void post(final String tag, final ApplicationEvent event) {
//		post(new EventType(tag, event.getClass()), event);
	}
//	
//	/**
//	 * 发布事件
//	 * @param eventType 事件封装
//	 */
//	@SuppressWarnings("unchecked")
//	private static void post(final EventType eventType, final ApplicationEvent event) {
//		EventKit.eventActuator(eventType, new EventCall() {
//			@Override
//			public void exec(ApplicationListener listener) {
//				listener.onApplicationEvent(event);
//			}
//		});
//	}
//	
//	public interface EventCall {
//		void exec(ApplicationListener listener);
//	}
//	
//	/**
//	 * 事件执行方法
//	 * @param eventType 事件类型
//	 * @param call EventCall
//	 * @return 
//	 */
//	private static void eventActuator(final EventType eventType, final EventCall call) {
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
//	}

}
