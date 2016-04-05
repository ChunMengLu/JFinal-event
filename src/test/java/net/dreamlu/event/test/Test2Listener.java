package net.dreamlu.event.test;

import net.dreamlu.event.core.ApplicationListener;
import net.dreamlu.event.core.Listener;

@Listener(order = 2)
public class Test2Listener implements ApplicationListener<Test2Event> {

	@Override
	public void onApplicationEvent(Test2Event event) {
		Integer xx = (Integer) event.getSource();
		System.out.println(Thread.currentThread().getName() + "\tsource:" + xx);
		
		// 新增
		EventType eventType = event.getEventType();
		System.out.println(eventType);
	}

}