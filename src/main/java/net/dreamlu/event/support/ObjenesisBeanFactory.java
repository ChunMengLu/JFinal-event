package net.dreamlu.event.support;

import com.jfinal.aop.Aop;
import com.jfinal.kit.LogKit;
import org.objenesis.ObjenesisStd;

/**
 * Objenesis bean 工厂
 *
 * 实现不需要默认构造器
 *
 * @author L.cm
 *
 */
public class ObjenesisBeanFactory extends DefaultBeanFactory {

	private final ObjenesisStd objenesis = new ObjenesisStd(true);

	@Override
	public <T> T getBean(Class<T> requiredType) throws Exception {
		// 如果 bean 已经被 jFinal 的 aop 初始化过，则直接获取
		try {
			return super.getBean(requiredType);
		} catch (Exception e) {
			LogKit.warn(e.getMessage(), e);
		}
		T bean = objenesis.newInstance(requiredType);
		return Aop.inject(bean);
	}

}
