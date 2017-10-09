package net.dreamlu.event.test;

import net.dreamlu.event.core.EventListener;

public class Test2SaveListener {

	@EventListener
	public void onApplicationEvent(Test2Event event) {
		Integer xx = (Integer) event.getSource();
		System.out.println(Thread.currentThread().getName() + " " + this.getClass() + " " + "\tsource:" + xx);
	}

}