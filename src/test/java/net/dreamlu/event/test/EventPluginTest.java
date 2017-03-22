package net.dreamlu.event.test;

import org.junit.Test;

import net.dreamlu.event.EventKit;
import net.dreamlu.event.EventPlugin;

public class EventPluginTest {

	@Test
	public void test1() {
		// 初始化插件
		EventPlugin plugin = new EventPlugin();
		// 全局开启异步
		plugin.async();

		// 设置扫描jar包，默认不扫描
		plugin.scanJar();
		// 设置默认扫描的包命，默认全扫描
		plugin.scanPackage("net.dreamlu");

		// 启动插件，用于main方法启动，jfinal中不需要，添加插件即可。
		plugin.start();
		
		// 发送第一个消息
		EventKit.post(new Test1Event("hello1"));
		// 发送第二个消息
		EventKit.post(new Test2Event(123123));
		
		// 发送带tag的消息
		EventKit.post("save", new Test2Event(123123));
		EventKit.post("update", new Test2Event(456456));
		
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
		EventPlugin plugin = new EventPlugin(false, "net.dreamlu");

		plugin.start();

		EventKit.post(new Test1Event("hello test3"));

		plugin.stop();
	}

	@Test
	public void test4() throws InterruptedException {
		EventPlugin plugin = new EventPlugin(false, "net.dreamlu", true);
		plugin.start();

		EventKit.post(new Test2Event(123123));
		System.out.println("begin xxxxx");
		Thread.sleep(500);
		System.out.println("end xxxxx");
		plugin.stop();
	}

}
