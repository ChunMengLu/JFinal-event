package net.dreamlu.event.holder;

import net.dreamlu.event.core.ApplicationEvent;

/**
 * 携带有控制器的Event
 * 
 * @author L.cm
 */
public class CtrlHolderEvent extends ApplicationEvent {
	private static final long serialVersionUID = 2175945135772538498L;
	
	private final CtrlHolder ctrlHolder;
	
	public CtrlHolderEvent(Object source) {
		super(source);
		this.ctrlHolder = CtrlHolderKit.get();
	}
	
	public CtrlHolder getCtrlHolder() {
		return ctrlHolder;
	}

}
