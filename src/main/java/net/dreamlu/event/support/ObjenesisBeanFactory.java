package net.dreamlu.event.support;

import org.objenesis.ObjenesisStd;

/**
 * Objenesis bean 工厂
 *
 * 实现不需要默认构造器
 *
 * @author L.cm
 *
 */
@Deprecated
public class ObjenesisBeanFactory extends DefaultBeanFactory {

	private final ObjenesisStd objenesis = new ObjenesisStd(true);

	@Override
	public <T> T getBean(Class<T> requiredType) throws Exception {
		return objenesis.newInstance(requiredType);
	}

}
