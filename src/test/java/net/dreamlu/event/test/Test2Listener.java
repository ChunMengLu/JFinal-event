package net.dreamlu.event.test;

import net.dreamlu.event.core.ApplicationListener;

public class Test2Listener implements ApplicationListener<Test2Event> {

	@Override
	public void onApplicationEvent(Test2Event event) {
		Integer xx = (Integer) event.getSource();
		System.out.println(Thread.currentThread().getName() + "\tsource:" + xx);
	}

}