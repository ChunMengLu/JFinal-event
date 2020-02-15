package com.demo;

import com.demo.common.DemoConfig;
import com.jfinal.server.undertow.UndertowServer;

public class DemoApplication {

	/**
	 * 启动入口，运行此 main 方法可以启动项目，此 main 方法可以放置在任意的 Class 类定义中，不一定要放于此
	 */
	public static void main(String[] args) {
		UndertowServer.start(DemoConfig.class);
	}

}
