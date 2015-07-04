package net.dreamlu.event.test;

import net.dreamlu.event.EventKit;
import net.dreamlu.event.EventPlugin;

import org.junit.Test;

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

		// 启动插件
		plugin.start();
		
		// 发送第一个消息
		EventKit.postEvent(new Test1Event("hello1"));
		// 发送第二个消息
		EventKit.postEvent(new Test2Event(123123));

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
		plugin.async(0);

		plugin.start();

		EventKit.postEvent(new Test1Event("hello test2"));

		plugin.stop();
	}

	@Test
	public void test3() {
		EventPlugin plugin = new EventPlugin(false, "net.dreamlu");

		plugin.start();

		EventKit.postEvent(new Test1Event("hello test3"));

		plugin.stop();
	}

	@Test
	public void test4() {
		EventPlugin plugin = new EventPlugin(false, "net.dreamlu", 2);

		plugin.start();

		EventKit.postEvent(new Test2Event(123123));

		plugin.stop();
	}

}
