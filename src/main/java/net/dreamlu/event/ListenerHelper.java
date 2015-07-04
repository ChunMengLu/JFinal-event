package net.dreamlu.event;

import net.dreamlu.event.core.ApplicationListener;

/**
 * 监听器工具类用于，用于数据暂存
 * @author L.cm
 * email: 596392912@qq.com
 * site:http://www.dreamlu.net
 * date 2015年7月4日上午10:30:07
 */
@SuppressWarnings("rawtypes")
class ListenerHelper {

	public final ApplicationListener listener;

	public final boolean enableAsync;

	public ListenerHelper(ApplicationListener listener, boolean enableAsync) {
		this.listener = listener;
		this.enableAsync = enableAsync;
	}

}
