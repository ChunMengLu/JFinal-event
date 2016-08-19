package net.dreamlu.event.test;

import net.dreamlu.event.core.ApplicationListener;
import net.dreamlu.event.core.Listener;

@Listener(order = 2, tag = "save")
public class Test2SaveListener implements ApplicationListener<Test2Event> {

	@Override
	public void onApplicationEvent(Test2Event event) {
		Integer xx = (Integer) event.getSource();
		System.out.println(Thread.currentThread().getName() + " " + this.getClass() + " " + "\tsource:" + xx);
	}

}