package net.dreamlu.event.test;

import net.dreamlu.event.core.ApplicationEvent;

public class Test1Event extends ApplicationEvent {

	private static final long serialVersionUID = 6994987952247306131L;

	private boolean exec = true;
	
	public Test1Event(Object source) {
		super(source);
	}

	public boolean isExec() {
		return exec;
	}

}