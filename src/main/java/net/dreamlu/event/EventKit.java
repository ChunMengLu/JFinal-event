package net.dreamlu.event;

import net.dreamlu.event.core.ApplicationEvent;


/**
 * 事件工具类
 * @author L.cm
 * email: 596392912@qq.com
 * site:http://www.dreamlu.net
 * @date 2015年4月26日下午9:58:53
 */
public class EventKit {

	private static EventHandler handler;

	static void init(EventHandler handler) {
		EventKit.handler = handler;
	}

	/**
	 * 发布事件
	 * @param event
	 */
	public static void postEvent(ApplicationEvent event) {
		handler.postEvent(event);
	}
}
