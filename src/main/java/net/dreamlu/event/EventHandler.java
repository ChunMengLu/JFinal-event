package net.dreamlu.event;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.concurrent.ExecutorService;

import net.dreamlu.event.core.ApplicationEvent;
import net.dreamlu.utils.ArrayListMultimap;

/**
 * 事件实际处理的类
 * @author L.cm
 * email: 596392912@qq.com
 * site:http://www.dreamlu.net
 * date 2015年4月26日下午10:02:46
 */
class EventHandler {

	private final ArrayListMultimap<Type, ListenerHelper> map;
	private final ExecutorService pool;

	EventHandler(ArrayListMultimap<Type, ListenerHelper> map,
			ExecutorService pool) {
		super();
		this.map = map;
		this.pool = pool;
	}

	/**
	 * 执行发送消息
	 * @param event ApplicationEvent
	 */
	@SuppressWarnings("unchecked")
	public void postEvent(final ApplicationEvent event) {
		Collection<ListenerHelper> listenerList = map.get(event.getClass());
		for (final ListenerHelper helper : listenerList) {
			if (null != pool && helper.enableAsync) {
				pool.execute(new Runnable() {

					@Override
					public void run() {
						helper.listener.onApplicationEvent(event);
					}
				});
			} else {
				helper.listener.onApplicationEvent(event);
			}
		}
	}
}
