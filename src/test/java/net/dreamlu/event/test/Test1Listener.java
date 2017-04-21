package net.dreamlu.event.test;

import net.dreamlu.event.core.ApplicationListener;
import net.dreamlu.event.core.Listener;

@Listener(order = 1, enableAsync = true)
public class Test1Listener implements ApplicationListener<Test1Event> {

	@Override
	public void onApplicationEvent(Test1Event event) {
		String xx = (String) event.getSource();
		System.out.println(Thread.currentThread().getName() + "\tsource:" + xx);
	}

}
