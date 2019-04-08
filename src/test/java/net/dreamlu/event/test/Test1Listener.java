package net.dreamlu.event.test;

import com.jfinal.kit.ElKit;
import com.jfinal.kit.Kv;

import net.dreamlu.event.core.EventListener;

public class Test1Listener {

	@EventListener(condition = "event.isExec()")
	public void xxx(Test1Event event) {
		String xx = event.getSource();
		System.out.println(Thread.currentThread().getName() + "\tsource:" + xx);
	}

	@EventListener
	public void xxx(Test2Event event) {
		if (event.getSource() instanceof Integer) {
			System.out.println(Thread.currentThread().getName() + "\tsource:" + event.getSource());
			throw new RuntimeException("测试抛出异常～～～");
		}
	}

	/**
	 * source event 测试
	 * @param event AccountEvent
	 */
	@EventListener
	public void xxx(AccountEvent event) {
		System.out.println(event);
	}

	public static void main(String[] args) {
		boolean x = ElKit.eval("event.isExec()", Kv.by("event", new Test1Event("hello")));
		System.out.println(x);
	}

}
