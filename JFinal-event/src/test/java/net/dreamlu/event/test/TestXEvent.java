package net.dreamlu.event.test;

import net.dreamlu.event.core.ApplicationEvent;

/**
 * 测试 不含有默认构造器的
 *
 * @author L.cm
 *
 */
public class TestXEvent extends ApplicationEvent<Object> {

	private static final long serialVersionUID = -2701800830876732190L;

	public TestXEvent(Object source) {
		super(source);
	}

}
