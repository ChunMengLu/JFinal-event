package net.dreamlu.event;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.dreamlu.event.core.ApplicationListener;
import net.dreamlu.event.core.Listener;
import net.dreamlu.utils.BeanUtil;
import net.dreamlu.utils.ClassUtil;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.jfinal.log.Logger;
import com.jfinal.plugin.IPlugin;

/**
 * 模拟spring的消息机制插件
 * @author L.cm
 * email: 596392912@qq.com
 * site:http://www.dreamlu.net
 * date 2015年4月26日下午10:25:04
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class EventPlugin implements IPlugin {

	private final Logger logger = Logger.getLogger(EventPlugin.class);

	// 转载所有的监听器，set排重
	private final Set<Class<? extends ApplicationListener>> allListeners;
	// guava重复key的map，使用监听的type，取出所有的监听器
	private final Multimap<Type, ApplicationListener> map;
	// 事件处理器
	private EventHandler handler = null;
	private ExecutorService pool = null;

	// 默认不扫描jar包
	private boolean scanJar = false;
	// 默认扫描所有的包
	private String scanPackage = "";

	/**
	 * 初始化实践插件
	 */
	public EventPlugin() {
		this.allListeners = new LinkedHashSet<Class<? extends ApplicationListener>>();
		this.map = ArrayListMultimap.create();
	}

	/**
	 * 异步，默认创建3个线程
	 * @param nThreads 默认线程池的容量
	 * @return EventPlugin
	 */
	public EventPlugin asyn(int... nThreads) {
		this.pool = Executors.newFixedThreadPool(nThreads.length==0 ? 3 : nThreads[0]);
		return this;
	}

	/**
	 * 从jar包中搜索监听器
	 * @return EventPlugin
	 */
	public EventPlugin scanJar() {
		this.scanJar = true;
		return this;
	}

	/**
	 * 指定扫描的包
	 * @param scanPackage 指定扫描的包
	 * @return EventPlugin
	 */
	public EventPlugin scanPackage(String scanPackage) {
		this.scanPackage = scanPackage;
		return this;
	}

	@Override
	public boolean start() {
		// 扫描注解 {@code Listener}
		Set<Class<?>> clazzSet = ClassUtil.scanPackageByAnnotation(scanPackage, scanJar, Listener.class);
		if (clazzSet.isEmpty()) {
			logger.warn("Listener is empty! Please check it!");
			return false;
		}
		// 装载所有 {@code ApplicationListener} 的子类
		Class superClass;
		for (Class<?> clazz : clazzSet) {
			superClass = ApplicationListener.class;
			if (superClass.isAssignableFrom(clazz) && !superClass.equals(clazz)) {
				allListeners.add((Class<? extends ApplicationListener>) clazz);
			}
		}
		if (allListeners.isEmpty()) {
			logger.warn("Listener is empty! Please check @Listener is right?");
			return false;
		}
		Type type;
		ApplicationListener listener;
		for (Class<? extends ApplicationListener> clazz : allListeners) {
			// 获取监听器上的泛型信息
			type = ((ParameterizedType) clazz.getGenericInterfaces()[0]).getActualTypeArguments()[0];
			// 实例化监听器
			listener = BeanUtil.newInstance(clazz);
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
