package net.dreamlu.event.test;

import net.dreamlu.event.core.EventListener;

public class Test3Listener {

	@EventListener
	public void onApplicationEvent(Test2Event event) {
		if (event.getSource() instanceof Integer) {
			System.out.println(Thread.currentThread().getName() + "\tsource:" + event.getSource());
			throw new RuntimeException("xxxx");
		}
	}

}