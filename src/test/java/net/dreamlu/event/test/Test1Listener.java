package net.dreamlu.event.test;

import net.dreamlu.event.core.ApplicationListener;

public class Test1Listener implements ApplicationListener<Test1Event> {

	@Override
	public void onApplicationEvent(Test1Event event) {
		String xx = (String) event.getSource();
		System.out.println(Thread.currentThread().getName() + "\tsource:" + xx);
	}

}
