package net.dreamlu.event.test;

import net.dreamlu.event.core.ApplicationEvent;
import net.dreamlu.event.core.Listener;

@Listener //错误的演示，@Listener请注解到ApplicationListener 的子类上
public class Test1Event extends ApplicationEvent {

	private static final long serialVersionUID = 6994987952247306131L;

	public Test1Event(Object source) {
		super(source);
	}

}
