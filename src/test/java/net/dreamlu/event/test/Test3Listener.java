package net.dreamlu.event.test;

import net.dreamlu.event.core.ApplicationListener;
import net.dreamlu.event.core.Listener;

@Listener(order = 3, enableAsync = true)
public class Test3Listener implements ApplicationListener<Test2Event> {

	@Override
	public void onApplicationEvent(Test2Event event) {
		if (event.getSource() instanceof Integer) {
			System.out.println(Thread.currentThread().getName() + "\tsource:" + event.getSource());
		}
	}

}