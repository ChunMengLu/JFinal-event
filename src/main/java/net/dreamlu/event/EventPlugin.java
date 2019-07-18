package net.dreamlu.event;

import com.jfinal.log.Log;
import com.jfinal.plugin.IPlugin;
import net.dreamlu.event.core.ApplicationListenerMethodAdapter;
import net.dreamlu.event.core.IBeanFactory;
import net.dreamlu.event.support.DefaultBeanFactory;
import net.dreamlu.processor.DreamEventsLoader;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
	private final List<ApplicationListenerMethodAdapter> listenerList;
	// 线程池
	private ExecutorService pool = null;
	// Bean工厂，方便扩展
	private IBeanFactory beanFactory;
	// 手动注册的监听类
	private Set<Class<?>> registeredClass = new HashSet<>();

	/**
	 * 构造EventPlugin
	 */
	public EventPlugin() {
		this.beanFactory = new DefaultBeanFactory();
		this.listenerList = new ArrayList<>();
	}

	/**
	 * 构造EventPlugin
	 * @param async 是否异步
	 */
	public EventPlugin(boolean async) {
		this();
		if (async) {
			async();
		}
	}

	/**
	 * 构造EventPlugin
	 * @param executorService 自定义线程池
	 */
	public EventPlugin(ExecutorService executorService) {
		this();
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
	 * 设定bean工厂
	 * @param beanFactory 设定bean工厂
	 * @return EventPlugin
	 */
	public EventPlugin beanFactory(IBeanFactory beanFactory) {
		this.beanFactory = beanFactory;
		return this;
	}

	/**
	 * 手动注册的监听类
	 * @param clazz 包含监听注解的类
	 * @return EventPlugin
	 */
	public EventPlugin register(Class<?> clazz) {
		registeredClass.add(clazz);
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
		if (!listenerList.isEmpty()) {
			return;
		}
		// 加载并包含手动注册的类
		Set<Method> methodSet = DreamEventsLoader.loadEventMethods(registeredClass);
		if (methodSet.isEmpty()) {
			log.warn("@EventListener is empty! Please check it!");
		}
		// 装载兼听
		List<ApplicationListenerMethodAdapter> allListeners = new ArrayList<>();
		for (Method method : methodSet) {
			Class<?> targetClass = method.getDeclaringClass();
			allListeners.add(new ApplicationListenerMethodAdapter(beanFactory, targetClass, method));
		}

		if (allListeners.isEmpty()) {
			log.warn("EventListener List is empty! Please check @EventListener is right?");
		}

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
		listenerList.clear();
		EventKit.cache.clear();
		return true;
	}

}
