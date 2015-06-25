package net.dreamlu.event.test;

import net.dreamlu.event.EventKit;
import net.dreamlu.event.EventPlugin;

public class TestEventPlugin {

	public static void main(String[] args) throws InterruptedException {
		// 初始化插件
		EventPlugin plugin = new EventPlugin();
		// 设置为异步
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

		Thread.sleep(1000);

		// 停止插件
		plugin.stop();
	}
//	http://jinnianshilongnian.iteye.com/blog/1902886

//	参考：
//	https://github.com/spring-projects/spring-framework/blob/master/spring-context/src/main/java/org/springframework/context/support/AbstractApplicationContext.java
//	https://github.com/spring-projects/spring-framework/blob/master/spring-context/src/main/java/org/springframework/context/event/AbstractApplicationEventMulticaster.java
//	https://github.com/spring-projects/spring-framework/blob/master/spring-context/src/main/java/org/springframework/context/event/SimpleApplicationEventMulticaster.java

//	Class 扫描：http://www.oschina.net/news/62178/hutool-2-8-1
}
