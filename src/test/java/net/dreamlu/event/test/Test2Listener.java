package net.dreamlu.event.test;

import net.dreamlu.event.core.ApplicationEvent;
import net.dreamlu.event.core.EventListener;

public class Test2Listener {

	@EventListener(order = 1, events = Test1Event.class)
	public void xxxx(ApplicationEvent event) {
		Object xx = event.getSource();
		System.out.println(Thread.currentThread().getName() + " ----- " + this.getClass() + " " + "\tsource:" + xx);
	}
	
	@EventListener(order = 1, events = Test2Event.class)
	public void xxxxx(ApplicationEvent event) {
		Object xx = event.getSource();
		System.out.println(Thread.currentThread().getName() + " xxxxx " + this.getClass() + " " + "\tsource:" + xx);
	}
	
	@EventListener
	public void xxxx(Test2Event event) {
		Integer xx = (Integer) event.getSource();
		System.out.println(Thread.currentThread().getName() + " " + this.getClass() + " " + "\tsource:" + xx);
	}

}