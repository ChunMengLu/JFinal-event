package net.dreamlu.event.test;

import net.dreamlu.event.core.ApplicationEvent;

public class Test1Event extends ApplicationEvent {

	private static final long serialVersionUID = 6994987952247306131L;

	public Test1Event(Object source) {
		super(source);
	}

}