package net.dreamlu.event.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

public class TestGuavaEvent {

	public static void main(String[] args) throws InterruptedException {
		ExecutorService pool = Executors.newFixedThreadPool(3);
		// 创建一个异步的消息
		EventBus bus = new AsyncEventBus(Executors.newFixedThreadPool(3));

		// Listener 的方法上注入 @Subscribe
		bus.register(new Test1Listener());
		bus.register(new Test2Listener());
		bus.register(new Test3Listener());

		bus.post(new Test1Event("event1"));

		bus.post(new Test2Event(22222222));

		bus = null;
		pool.shutdown();
	}

//	参考:
//	https://code.google.com/p/guava-libraries/wiki/EventBusExplained

//  Why use an annotation to mark handler methods, rather than requiring the listener to implement an interface?
}
