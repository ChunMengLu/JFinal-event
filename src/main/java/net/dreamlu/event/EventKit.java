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

	private static EventHander hander;

	static void init(EventHander hander) {
		EventKit.hander = hander;
	}

	/**
	 * 发布事件
	 * @param event
	 */
	public static void postEvent(ApplicationEvent event) {
		hander.postEvent(event);
	}
}
