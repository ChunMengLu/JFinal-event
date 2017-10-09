package net.dreamlu.event;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.jfinal.log.Log;
import com.jfinal.plugin.IPlugin;

import net.dreamlu.event.core.ApplicationListener;
import net.dreamlu.event.core.EventListener;
import net.dreamlu.utils.ArrayListMultimap;
import net.dreamlu.utils.BeanUtil;
import net.dreamlu.utils.ClassUtil;

/**
 * 模拟spring的消息机制插件
 * @author L.cm
 * email: 596392912@qq.com
 * site:http://www.dreamlu.net
 * date 2015年4月26日下午10:25:04
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class EventPlugin implements IPlugin {
	private static Log log = Log.getLog(ClassUtil.class);
	// 线程池
	private static ExecutorService pool = null;
	// 重复key的map，使用监听的type，取出所有的监听器
	private static ArrayListMultimap<Type, ApplicationListener> map = null;
	// 默认不扫描jar包
	private boolean scanJar = false;
	// 默认扫描所有的包
	private String scanPackage = "";

	/**
	 * 构造EventPlugin
	 */
	public EventPlugin() {}
	
	/**
	 * 构造EventPlugin
	 * @param scanJar 是否扫描jar
	 * @param scanPackage 扫描的包名
	 */
	public EventPlugin(boolean scanJar, String scanPackage) {
		this.scanJar = scanJar;
		this.scanPackage = scanPackage;
	}
	
	/**
	 * 构造EventPlugin
	 * @param scanJar 是否扫描jar
	 * @param scanPackage 扫描的包名
	 * @param async 是否异步
	 */
	public EventPlugin(boolean scanJar, String scanPackage, boolean async) {
		this.scanJar = scanJar;
		this.scanPackage = scanPackage;
		if (async) {
			async();
		}
	}
	
	/**
	 * 构造EventPlugin
	 * @param scanJar 是否扫描jar
	 * @param scanPackage 扫描的包名
	 * @param executorService 自定义线程池
	 */
	public EventPlugin(boolean scanJar, String scanPackage, ExecutorService executorService) {
		this.scanJar = scanJar;
		this.scanPackage = scanPackage;
		pool = executorService;
	}
	
	/**
	 * 异步，默认SingleThreadExecutor
	 * @return EventPlugin
	 */
	public EventPlugin async() {
		if (pool == null) {
			pool = Executors.newSingleThreadExecutor(new EventThreadFactory());
		}
		return this;
	}
	
	/**
	 * 自定义线程池
	 * @param executorService 线程池
	 * @return EventPlugin
	 */
	public EventPlugin threadPool(ExecutorService executorService) {
		pool = executorService;
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
		create();
//		EventKit.init(map, pool);
		return true;
	}

	/**
	 * 构造
	 */
	private void create() {
		if (null != map) {
			return;
		}
		// 扫描注解 {@code Listener}
		Set<Class<?>> clazzSet = ClassUtil.scanPackageByAnnotation(scanPackage, scanJar, EventListener.class);
		if (clazzSet.isEmpty()) {
			log.error("Listener is empty! Please check it!");
		}

		List<Class<? extends ApplicationListener>> allListeners = new ArrayList<Class<? extends ApplicationListener>>();
		// 装载所有 {@code ApplicationListener} 的子类
		Class superClass;
		for (Class<?> clazz : clazzSet) {
			superClass = ApplicationListener.class;
			if (superClass.isAssignableFrom(clazz) && !superClass.equals(clazz)) {
				allListeners.add((Class<? extends ApplicationListener>) clazz);
			}
		}
		if (allListeners.isEmpty()) {
			log.error("Listener is empty! Please check @Listener is right?");
		}

		// 监听器排序
		sortListeners(allListeners);

		// 重复key的map，使用监听的type，取出所有的监听器
//		map = new ArrayListMultimap<EventType, ListenerHelper>();
//
//		Type type;
//		ApplicationListener listener;
//		for (Class<? extends ApplicationListener> clazz : allListeners) {
//			// 获取监听器上的泛型信息
//			type = ((ParameterizedType) clazz.getGenericInterfaces()[0]).getActualTypeArguments()[0];
//			// 实例化监听器
//			listener = BeanUtil.newInstance(clazz);
//
//			// 监听器上的注解
//			EventListener annotation = clazz.getAnnotation(EventListener.class);
//			boolean enableAsync = annotation.async();
//			EventType eventType = new EventType(tag, type);
//			map.put(eventType, new ListenerHelper(listener, enableAsync));
//			if (log.isDebugEnabled()) {
//				log.debug(clazz + " init~");
//			}
//		}
	}

	/**
	 * 对所有的监听器进行排序
	 */
	private void sortListeners(List<Class<? extends ApplicationListener>> listeners) {
		Collections.sort(listeners, new Comparator<Class<? extends ApplicationListener>>() {

			@Override
			public int compare(Class<? extends ApplicationListener> o1,
					Class<? extends ApplicationListener> o2) {

				int x = o1.getAnnotation(EventListener.class).order();
				int y = o2.getAnnotation(EventListener.class).order();
				return (x < y) ? -1 : ((x == y) ? 0 : 1);
			}
		});
	}

	@Override
	public boolean stop() {
		if (null != pool) {
			pool.shutdown();
			pool = null;
		}
		if (null != map) {
			map.clear();
			map = null;
		}
		return true;
	}

}
