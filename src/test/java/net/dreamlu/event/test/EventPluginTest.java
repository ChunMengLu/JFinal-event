package net.dreamlu.event.test;

import java.util.concurrent.Executors;

import org.junit.Test;

import net.dreamlu.event.EventKit;
import net.dreamlu.event.EventPlugin;
import net.dreamlu.event.EventThreadFactory;

public class EventPluginTest {

	@Test(expected = RuntimeException.class)
	public void test1() {
		// 初始化插件
		EventPlugin plugin = new EventPlugin();
		// 全局开启异步,默认设置SingleThreadExecutor线程池
		plugin.async();
		//手动设置线程池与async()互斥，只需要设置一个即可
		//plugin.threadPool(Executors.newCachedThreadPool(new EventThreadFactory()));
		// EventThreadFactory 中对异常进行了处理，避免影响控制器中的请求

		// 启动插件，用于main方法启动，jfinal中不需要，添加插件即可。
		plugin.start();

		// 发送第一个消息
		EventKit.post(new Test1Event("hello1"));
		// 发送第二个消息
		EventKit.post(new Test2Event(123123));

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// 停止插件
		plugin.stop();
	}

	@Test
	public void test2() {
		EventPlugin plugin = new EventPlugin();
		plugin.async();
		plugin.start();
		EventKit.post(new Test1Event("hello test2"));
		plugin.stop();
	}

	@Test
	public void test3() {
		EventPlugin plugin = new EventPlugin(false);
		plugin.async();
		plugin.start();

		EventKit.post(new Test1Event("hello test3"));

		plugin.stop();
	}

	@Test(expected = RuntimeException.class)
	public void test4() throws InterruptedException {
		EventPlugin plugin = new EventPlugin(Executors.newCachedThreadPool(new EventThreadFactory()));
		plugin.start();

		EventKit.post(new Test2Event(123123));
		for (int i = 0; i < 100; i++) {
			EventKit.post(new Test2Event(i));
		}
		System.out.println("begin xxxxx");
		Thread.sleep(500);
		System.out.println("end xxxxx");
		plugin.stop();
	}

	/**
	 * 测试不含默认构造器
	 *
	 * DefaultBeanFactory
	 */
	@Test(expected = RuntimeException.class)
	public void testX1() {
		EventPlugin plugin = new EventPlugin();
		plugin.start();
		EventKit.post(new TestXEvent(10000));
		plugin.stop();
	}

	/**
	 * 测试不含默认构造器
	 *
	 * ObjenesisBeanFactory
	 */
	@Test
	public void testSource() {
		EventPlugin plugin = new EventPlugin();
		plugin.start();

		AccountEvent event = new AccountEvent();
		event.setId(1);
		event.setName("张三");
		event.setAge(18);

		EventKit.post(event);
		plugin.stop();
	}
}
