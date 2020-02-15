package net.dreamlu.event.test;

import net.dreamlu.event.core.ApplicationEvent;

public class Test2Event extends ApplicationEvent<Integer> {

	private static final long serialVersionUID = 6994987952247306131L;

	// 构造函数中限定source的类型，避免传错参数
	public Test2Event(Integer source) {
		super(source);
	}

}
