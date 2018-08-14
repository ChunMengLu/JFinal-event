package net.dreamlu.event.test;

import net.dreamlu.event.core.ApplicationEvent;
import net.dreamlu.event.core.EventListener;

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

	@EventListener
	public void test3(TestXEvent event) {
		Integer xx = (Integer) event.getSource();
		System.out.println(Thread.currentThread().getName() + " " + this.getClass() + " " + "\tsource:" + xx);
	}
}