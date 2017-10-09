package net.dreamlu.event.test;

import com.jfinal.kit.ElKit;
import com.jfinal.kit.Kv;

import net.dreamlu.event.core.EventListener;

public class Test1Listener {

	@EventListener(condition = "event.isExec()")
	public void xxx(Test1Event event) {
		String xx = (String) event.getSource();
		System.out.println(Thread.currentThread().getName() + "\tsource:" + xx);
	}

	public static void main(String[] args) {
		boolean x = ElKit.eval("event.isExec()", Kv.by("event", new Test1Event("hello")));
		System.out.println(x);
	}
}
