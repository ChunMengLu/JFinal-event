package net.dreamlu.event.core;

import com.jfinal.kit.ElKit;
import com.jfinal.kit.Kv;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * 监听器封装
 *
 * @author L.cm
 * email: 596392912@qq.com
 * site:http://www.dreamlu.net
 * date 2017年10月10日上午11:27:24
 */
public class ApplicationListenerMethodAdapter implements ApplicationListener<Object> {
	private static Log log = Log.getLog(ApplicationListenerMethodAdapter.class);

	private final IBeanFactory beanFactory;
	private final Method method;
	private final int paramCount;
	private final Class<?> paramType;
	/**
	 * 参数为全能的类型
	 */
	private final boolean isAlmightyParam;
	private final Class<?> targetClass;
	private final List<Class<?>> declaredEventClasses;
	private final String condition;
	private final int order;
	private final boolean async;

	public ApplicationListenerMethodAdapter(IBeanFactory beanFactory, Class<?> targetClass, Method method) {
		this.beanFactory = beanFactory;
		this.method = method;
		this.paramCount = method.getParameterCount();
		this.paramType = this.paramCount > 0 ? method.getParameterTypes()[0] : null;
		this.isAlmightyParam = Object.class == this.paramType;
		this.targetClass = targetClass;
		// 事件注解上的信息
		EventListener ann = method.getAnnotation(EventListener.class);
		// 支持 EventListener value() 和 events()
		this.declaredEventClasses = ApplicationListenerMethodAdapter.join(ann.value(), ann.events());
		this.condition = ann.condition();
		this.order = ann.order();
		this.async = ann.async();
	}

	@Override
	public void onApplicationEvent(Object event) {
		// 判断表达式
		if (StrKit.notBlank(this.condition)) {
			boolean elPass = ElKit.eval(this.condition, Kv.by("event", event));
			log.debug("method:[" + this.method + "]-condition:[" + this.condition + "]-result:[" + elPass + "]");
			if (!elPass) {
				return;
			}
		}
		try {
			Object bean = beanFactory.getBean(this.targetClass);
			Object[] args;
			// 兼容没有参数的监听器
			if (this.paramCount == 0) {
				args = new Object[0];
			} else {
				args = new Object[]{event};
			}
			this.method.invoke(bean, args);
		} catch (Exception e) {
			throw handleException(e);
		}
	}

	public Method getMethod() {
		return method;
	}

	public int getParamCount() {
		return paramCount;
	}

	public Class<?> getParamType() {
		return paramType;
	}

	public boolean isAlmightyParam() {
		return isAlmightyParam;
	}

	public Class<?> getTargetClass() {
		return targetClass;
	}

	public List<Class<?>> getDeclaredEventClasses() {
		return declaredEventClasses;
	}

	public String getCondition() {
		return condition;
	}

	public int getOrder() {
		return order;
	}

	public boolean isAsync() {
		return async;
	}

	@Override
	public String toString() {
		return "@EventListener [" + method + "]";
	}

	/**
	 * 组合数组
	 *
	 * @param first  数组1
	 * @param second 数组2
	 * @return 组合后的数组
	 */
	private static List<Class<?>> join(Class<?>[] first, Class<?>[] second) {
		Class<?>[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return Arrays.asList(result);
	}

	/**
	 * 将CheckedException转换为UncheckedException.
	 *
	 * @param e Throwable
	 * @return {RuntimeException}
	 */
	private static RuntimeException handleException(Throwable e) {
		if (e instanceof Error) {
			throw (Error) e;
		} else if (e instanceof IllegalAccessException ||
			e instanceof IllegalArgumentException ||
			e instanceof NoSuchMethodException) {
			return new IllegalArgumentException(e);
		} else if (e instanceof InvocationTargetException) {
			return runtime(((InvocationTargetException) e).getTargetException());
		} else if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		} else if (e instanceof InterruptedException) {
			Thread.currentThread().interrupt();
		}
		return runtime(e);
	}

	/**
	 * 不采用 RuntimeException 包装，直接抛出，使异常更加精准
	 *
	 * @param throwable Throwable
	 * @param <T>       泛型标记
	 * @return Throwable
	 * @throws T 泛型
	 */
	@SuppressWarnings("unchecked")
	private static <T extends Throwable> T runtime(Throwable throwable) throws T {
		throw (T) throwable;
	}

}
