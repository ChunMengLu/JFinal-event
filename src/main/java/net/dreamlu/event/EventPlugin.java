package net.dreamlu.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.jfinal.log.Log;
import com.jfinal.plugin.IPlugin;

import net.dreamlu.event.core.ApplicationListenerMethodAdapter;
import net.dreamlu.event.core.EventListener;
import net.dreamlu.event.core.IBeanFactory;
import net.dreamlu.event.support.DefaultBeanFactory;
import net.dreamlu.utils.ClassUtil;

/**
 * 模拟spring的消息机制插件
 * @author L.cm
 * email: 596392912@qq.com
 * site:http://www.dreamlu.net
 * date 2015年4月26日下午10:25:04
 */
public class EventPlugin implements IPlugin {
	private static Log log = Log.getLog(EventPlugin.class);
	// 事件监听处理器
	private static List<ApplicationListenerMethodAdapter> listenerList = null;
	// 线程池
	private static ExecutorService pool = null;
	// 默认不扫描jar包
	private boolean scanJar = false;
	// 默认扫描所有的包
	private String scanPackage = "";
	// Bean工厂，方便扩展
	private IBeanFactory beanFactory;

	/**
	 * 构造EventPlugin
	 */
	public EventPlugin() {
		this.beanFactory = new DefaultBeanFactory();
	}
	
	/**
	 * 构造EventPlugin
	 * @param scanJar 是否扫描jar
	 * @param scanPackage 扫描的包名
	 */
	public EventPlugin(boolean scanJar, String scanPackage) {
		this();
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
		this(scanJar, scanPackage);
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
		this(scanJar, scanPackage);
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
	
	/**
	 * 设定bean工厂
	 * @param beanFactory 设定bean工厂
	 * @return EventPlugin
	 */
	public EventPlugin beanFactory(IBeanFactory beanFactory) {
		this.beanFactory = beanFactory;
		return this;
	}
	
	@Override
	public boolean start() {
		create();
		EventKit.init(listenerList, pool);
		return true;
	}

	/**
	 * 构造
	 */
	private void create() {
		if (null != listenerList) {
			return;
		}
		// 扫描注解 {@code EventListener}
		MethodEventFilter filter = new MethodEventFilter(EventListener.class);
		ClassUtil.scanPackage(scanPackage, scanJar, filter);
		
		Set<Method> methodSet = filter.getListeners();
		if (methodSet.isEmpty()) {
			log.warn("@EventListener is empty! Please check it!");
		}
		
		// 装载兼听
		List<ApplicationListenerMethodAdapter> allListeners = new ArrayList<ApplicationListenerMethodAdapter>();
		for (Method method : methodSet) {
			Class<?> targetClass = method.getDeclaringClass();
			allListeners.add(new ApplicationListenerMethodAdapter(beanFactory, targetClass, method));
		}
		
		if (allListeners.isEmpty()) {
			log.warn("EventListener List is empty! Please check @EventListener is right?");
		}
		
		listenerList = new ArrayList<ApplicationListenerMethodAdapter>();
		for (ApplicationListenerMethodAdapter applicationListener : allListeners) {
			listenerList.add(applicationListener);
			if (log.isDebugEnabled()) {
				log.debug(applicationListener + " init~");
			}
		}
	}

	@Override
	public boolean stop() {
		if (null != pool) {
			pool.shutdown();
			pool = null;
		}
		if (null != listenerList) {
			listenerList.clear();
			listenerList = null;
		}
		EventKit.cache.clear();
		return true;
	}

}
