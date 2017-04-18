package net.dreamlu.event.test;

import java.rmi.RemoteException;

import net.dreamlu.event.EventKit;
import net.dreamlu.event.EventPlugin;
import net.dreamlu.event.rmi.RmiClientConfig;
import net.dreamlu.event.rmi.RmiServerConfig;

public class RmiTest {
	public static void main(String[] args) throws RemoteException, InterruptedException {
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
		plugin.scanPackage("net.dreamlu");
		
		plugin.setRmiServerConfig(new RmiServerConfig(8880));
		Thread.sleep(500);
		plugin.setRmiClientConfig(new RmiClientConfig(8880, "localhost"));
		
		// 启动插件，用于main方法启动，jfinal中不需要，添加插件即可。
		plugin.start();
		// 发送第一个消息
		EventKit.postRemote(new Test1Event("hello1"));
		
		Thread.sleep(2000);
		plugin.stop();
	}
}
