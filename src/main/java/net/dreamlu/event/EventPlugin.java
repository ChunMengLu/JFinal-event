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

import net.dreamlu.event.core.ApplicationListener;
import net.dreamlu.event.core.Listener;
import net.dreamlu.utils.ArrayListMultimap;
import net.dreamlu.utils.BeanUtil;
import net.dreamlu.utils.ClassUtil;

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

	// 线程池
	private static ExecutorService pool = null;
	// 重复key的map，使用监听的type，取出所有的监听器
	private static ArrayListMultimap<Type, ListenerHelper> map = null;

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
	 * @param asyncThreads 异步的线程池的大小，不传、小于或者等于0时不开启
	 */
	public EventPlugin(boolean scanJar, String scanPackage, int... asyncThreads) {
		this.scanJar = scanJar;
		this.scanPackage = scanPackage;
		if (asyncThreads.length > 0 && asyncThreads[0] > 0) {
			async(asyncThreads);
		}
	}

	/**
	 * 异步，默认创建3个线程
	 * @param nThreads 线程池的容量，不传或小于1时默认为3
	 * @return EventPlugin
	 */
	public EventPlugin async(int... nThreads) {
		pool = Executors.newFixedThreadPool(nThreads.length == 0 || nThreads[0] < 1 ? 3 : nThreads[0]);
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
		EventKit.init(map, pool);
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
		Set<Class<?>> clazzSet = ClassUtil.scanPackageByAnnotation(scanPackage, scanJar, Listener.class);
		if (clazzSet.isEmpty()) {
			logger.warn("Listener is empty! Please check it!");
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
			logger.warn("Listener is empty! Please check @Listener is right?");
		}

		// 监听器排序
		sortListeners(allListeners);

		// 重复key的map，使用监听的type，取出所有的监听器
		map = new ArrayListMultimap<Type, ListenerHelper>();

		Type type;
		ApplicationListener listener;
		for (Class<? extends ApplicationListener> clazz : allListeners) {
			// 获取监听器上的泛型信息
			type = ((ParameterizedType) clazz.getGenericInterfaces()[0]).getActualTypeArguments()[0];
			// 实例化监听器
			listener = BeanUtil.newInstance(clazz);

			boolean enableAsync = clazz.getAnnotation(Listener.class).enableAsync();

			map.put(type, new ListenerHelper(listener, enableAsync));
			logger.debug(clazz + " init~");
		}

	}

	/**
	 * 对所有的监听器进行排序
	 */
	private void sortListeners(List<Class<? extends ApplicationListener>> listeners) {
		Collections.sort(listeners, new Comparator<Class<? extends ApplicationListener>>() {

			@Override
			public int compare(Class<? extends ApplicationListener> o1,
					Class<? extends ApplicationListener> o2) {

				int order1 = o1.getAnnotation(Listener.class).order();
				int order2 = o2.getAnnotation(Listener.class).order();
				return Integer.compare(order1, order2);
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
