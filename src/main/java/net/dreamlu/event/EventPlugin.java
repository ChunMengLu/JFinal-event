package net.dreamlu.event;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.dreamlu.event.core.ApplicationListener;
import net.dreamlu.utils.BeanUtils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.jfinal.log.Logger;
import com.jfinal.plugin.IPlugin;

/**
 * 模拟spring的消息机制插件
 * @author L.cm
 * email: 596392912@qq.com
 * site:http://www.dreamlu.net
 * @date 2015年4月26日下午10:25:04
 */
@SuppressWarnings("rawtypes")
public class EventPlugin implements IPlugin {

	private final Logger logger = Logger.getLogger(EventPlugin.class);

	// 转载所有的监听器，set排重
	private final Set<Class<? extends ApplicationListener>> allListeners;
	// guava重复key的map，使用监听的type，取出所有的监听器
	private final Multimap<Type, ApplicationListener> map;
	// 事件处理器
	private EventHandler handler = null;
	private ExecutorService pool = null;
	
	/**
	 * 初始化实践插件
	 */
	public EventPlugin() {
		this.allListeners = new LinkedHashSet<Class<? extends ApplicationListener>>();
		this.map = ArrayListMultimap.create();
	}

	/**
	 * 异步，默认创建3个线程
	 * @return
	 */
	public EventPlugin asyn(int... nThreads) {
		pool = Executors.newFixedThreadPool(nThreads.length==0 ? 3 : nThreads[0]);
		return this;
	}

	/**
	 * 添加监听
	 * @param listener
	 * @return
	 */
	public EventPlugin addListener(Class<? extends ApplicationListener> listener) {
		allListeners.add(listener);
		return this;
	}

	@Override
	public boolean start() {
		if (allListeners.isEmpty()) {
			logger.warn("Listener is empty! Please addListener befor start~~~");
			return false;
		}
		Type type;
		ApplicationListener listener;
		for (Class<? extends ApplicationListener> clazz : allListeners) {
			// 获取监听器上的泛型信息
			type = ((ParameterizedType) clazz.getGenericInterfaces()[0]).getActualTypeArguments()[0];
			// 实例化监听器
			listener = BeanUtils.newInstance(clazz);
			map.put(type, listener);
		}
		handler = new EventHandler(map, pool);
		EventKit.init(handler);
		return true;
	}

	@Override
	public boolean stop() {
		allListeners.clear();
		map.clear();
		if (null != pool) {
			pool.shutdown();
		}
		pool = null;
		handler = null;
		return true;
	}

}
