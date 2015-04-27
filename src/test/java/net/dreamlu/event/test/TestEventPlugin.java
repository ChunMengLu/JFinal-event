package net.dreamlu.event.test;

import net.dreamlu.event.EventKit;
import net.dreamlu.event.EventPlugin;

public class TestEventPlugin {

	public static void main(String[] args) throws InterruptedException {
		EventPlugin plugin = new EventPlugin();

		plugin.asyn();

		plugin.addListener(Test1Listener.class);
		plugin.addListener(Test2Listener.class);
		plugin.addListener(Test3Listener.class);

		plugin.start();

		EventKit.postEvent(new Test1Event("hello1"));
		EventKit.postEvent(new Test2Event(123123));
		Thread.sleep(1000);

		plugin.stop();
	}
//	http://jinnianshilongnian.iteye.com/blog/1902886

//	参考：
//	https://github.com/spring-projects/spring-framework/blob/master/spring-context/src/main/java/org/springframework/context/support/AbstractApplicationContext.java
//	https://github.com/spring-projects/spring-framework/blob/master/spring-context/src/main/java/org/springframework/context/event/AbstractApplicationEventMulticaster.java
//	https://github.com/spring-projects/spring-framework/blob/master/spring-context/src/main/java/org/springframework/context/event/SimpleApplicationEventMulticaster.java

}
