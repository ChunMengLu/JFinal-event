package net.dreamlu.event.core;

/**
 * 泛型事件
 *
 * @param <T> 泛型标记
 * @author L.cm
 */
public class SourceApplicationEvent<T> extends ApplicationEvent<T> {

	/**
	 * 泛型事件构造器
	 *
	 * @param source 事件源
	 */
	public SourceApplicationEvent(T source) {
		super(source);
	}

	public Class<?> getSourceClass() {
		return super.source.getClass();
	}

}
