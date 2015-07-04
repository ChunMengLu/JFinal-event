package net.dreamlu.event.test;

import org.junit.Before;
import org.junit.Test;

import net.dreamlu.event.EventKit;
import net.dreamlu.event.EventPlugin;

public class EventPluginTest {

	EventPlugin plugin = null;

	/**
	 * 配置并启动插件
	 */
	@Before
	public void setUp() {
		// 初始化插件
		plugin = new EventPlugin();
		// 全局开启异步
		plugin.async();

		// 设置扫描jar包，默认不扫描
		plugin.scanJar();
		// 设置默认扫描的包命，默认全扫描
		plugin.scanPackage("net.dreamlu");

		// 启动插件
		plugin.start();
	}

	@Test
	public void test() {
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
}
