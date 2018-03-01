package net.dreamlu.event.holder;

/**
 * 控制器Holder工具，用于获取控制器
 * 
 * @author L.cm
 */
public class CtrlHolderKit {
	private static final ThreadLocal<CtrlHolder> tl = new ThreadLocal<CtrlHolder>();
	
	static void set(CtrlHolder value) {
		tl.set(value);
	}
	
	static void remove() {
		CtrlHolder holder = get();
		holder.requestCompleted();
		tl.remove();
	}
	
	public static CtrlHolder get() {
		return tl.get();
	}
}
