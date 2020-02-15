package net.dreamlu.event.holder;

/**
 * 控制器Holder工具，用于获取控制器
 * 
 * @author L.cm
 */
public class CtrlHolderKit {
	private static final ThreadLocal<CtrlHolder> TL = new ThreadLocal<CtrlHolder>();
	
	static void set(CtrlHolder value) {
		TL.set(value);
	}
	
	static void remove() {
		CtrlHolder holder = get();
		holder.requestCompleted();
		TL.remove();
	}
	
	public static CtrlHolder get() {
		return TL.get();
	}
}
