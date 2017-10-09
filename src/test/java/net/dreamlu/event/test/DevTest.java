package net.dreamlu.event.test;

import java.util.List;

import net.dreamlu.event.MethodEventFilter;
import net.dreamlu.event.core.ApplicationListenerMethodAdapter;
import net.dreamlu.event.core.EventListener;
import net.dreamlu.utils.ClassUtil;

public class DevTest {

	public static void main(String[] args) {
		MethodEventFilter f = new MethodEventFilter(EventListener.class);
		
		ClassUtil.scanPackage("", true, f);
		
		List<ApplicationListenerMethodAdapter> list = f.getListenerList();
		
		System.out.println(list.size());
		
		for (ApplicationListenerMethodAdapter a : list) {
			System.out.println(a);
		}
	}

}
