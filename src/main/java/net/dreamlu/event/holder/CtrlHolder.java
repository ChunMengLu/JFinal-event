package net.dreamlu.event.holder;

import com.jfinal.core.Controller;

/**
 * 控制器Holder
 * 
 * @author L.cm
 */
class CtrlHolder {
	private final Controller ctrl;
	private volatile boolean requestActive = true;
	
	public CtrlHolder(Controller ctrl) {
		this.ctrl = ctrl;
	}
	
	public Controller getCtrl() {
		return ctrl;
	}
	
	public boolean isRequestActive() {
		return requestActive;
	}
	
	public void requestCompleted() {
		this.requestActive = false;
	}
}
