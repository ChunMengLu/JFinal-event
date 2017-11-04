package net.dreamlu.event.test;

import java.util.concurrent.Executors;

import org.junit.Test;

import net.dreamlu.event.EventKit;
import net.dreamlu.event.EventPlugin;
import net.dreamlu.event.EventThreadFactory;
import net.dreamlu.event.support.DuangBeanFactory;

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
		
		// 设置扫描jar包，默认不扫描
		plugin.scanJar();
		// 设置默认扫描的包命，默认全扫描
		plugin.scanPackage("net.dreamlu.a;net.dreamlu.b;net.dreamlu");

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
		plugin.beanFactory(new DuangBeanFactory());
		plugin.async();

		plugin.start();

		EventKit.post(new Test1Event("hello test2"));

		plugin.stop();
	}

	@Test
	public void test3() {
		EventPlugin plugin = new EventPlugin(false, "net.dreamlu");
		plugin.async();
		plugin.start();

		EventKit.post(new Test1Event("hello test3"));

		plugin.stop();
	}

	@Test(expected = RuntimeException.class)
	public void test4() throws InterruptedException {
		EventPlugin plugin = new EventPlugin(false, "net.dreamlu", Executors.newCachedThreadPool(new EventThreadFactory()));
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

}
