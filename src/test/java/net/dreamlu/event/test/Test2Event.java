package net.dreamlu.event.test;

import net.dreamlu.event.core.ApplicationEvent;

public class Test2Event extends ApplicationEvent {

	private static final long serialVersionUID = 6994987952247306131L;

	// 构造函数中限定source的类型，避免传错参数
	public Test2Event(Integer source) {
		super(source);
	}

	//-------------------------------------------------------//
	// 枚举类型，类型你自己去定义，你可以根据你的业务来构造，比如说 Order，Email等
	private EventType eventType;
	// 新增的构造器
	public Test2Event(Object source, EventType eventType) {
		super(source);
		this.eventType = eventType;
	}

	public EventType getEventType() {
		return eventType;
	}
	//-------------------------------------------------------//
}
