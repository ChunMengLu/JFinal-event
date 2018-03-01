package net.dreamlu.event.holder;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

/**
 * 控制器Holder拦截器
 * 
 * @author L.cm
 */
public class CtrlHolderInterceptor implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		Controller ctl = inv.getController();
		try {
			CtrlHolderKit.set(new CtrlHolder(ctl));
			inv.invoke();
		} finally {
			CtrlHolderKit.remove();
		}
	}

}
