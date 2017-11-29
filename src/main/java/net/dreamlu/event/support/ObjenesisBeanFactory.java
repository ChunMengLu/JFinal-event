package net.dreamlu.event.support;

import org.objenesis.ObjenesisStd;

import net.dreamlu.event.core.IBeanFactory;

/**
 * Objenesis bean 工厂
 * 
 * 实现不需要默认构造器
 * 
 * @author L.cm
 *
 */
public class ObjenesisBeanFactory implements IBeanFactory {

	private final ObjenesisStd objenesis = new ObjenesisStd(true);
	
	@Override
	public <T> T getBean(Class<T> requiredType) throws Exception {
		return objenesis.newInstance(requiredType);
	}

}
