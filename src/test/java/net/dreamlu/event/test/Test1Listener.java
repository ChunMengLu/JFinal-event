package net.dreamlu.event.test;

import net.dreamlu.event.core.EventListener;

public class Test1Listener {

	@EventListener()
	public void onApplicationEvent(Test1Event event) {
		String xx = (String) event.getSource();
		System.out.println(Thread.currentThread().getName() + "\tsource:" + xx);
	}

}
