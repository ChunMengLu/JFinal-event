package net.dreamlu.event.support;

import com.jfinal.aop.Duang;

import net.dreamlu.event.core.IBeanFactory;

/**
 * JFinal的Duang Bean工厂
 * @author L.cm
 * email: 596392912@qq.com
 * site:http://www.dreamlu.net
 * date 2017年10月11日下午9:58:53
 *
 */
public class DuangBeanFactory implements IBeanFactory {

	@Override
	public <T> T getBean(Class<T> requiredType) throws Exception {
		return Duang.duang(requiredType);
	}

}
