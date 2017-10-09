package net.dreamlu.event.test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import net.dreamlu.event.MethodEventFilter;
import net.dreamlu.event.core.ApplicationListenerMethodAdapter;
import net.dreamlu.event.core.EventListener;
import net.dreamlu.utils.ClassUtil;

public class DevTest {

	public static void main(String[] args) {
		MethodEventFilter filter = new MethodEventFilter(EventListener.class);
		
		ClassUtil.scanPackage("", true, filter);
		
		Set<Method> set = filter.getListeners();
		
		System.out.println(set.size());
		
		List<ApplicationListenerMethodAdapter> list = new ArrayList<ApplicationListenerMethodAdapter>();
		for (Method method : set) {
			Class<?> targetClass = method.getDeclaringClass();
			list.add(new ApplicationListenerMethodAdapter(targetClass, method));
		}
		Collections.sort(list, new Comparator<ApplicationListenerMethodAdapter>() {
			@Override
			public int compare(ApplicationListenerMethodAdapter o1, ApplicationListenerMethodAdapter o2) {
				int x = o1.getOrder(); int y = o2.getOrder();
				return (x < y) ? -1 : ((x == y) ? 0 : 1);
			}
		});
		for (ApplicationListenerMethodAdapter ama : list) {
			System.out.println(ama.getMethod());
			ama.onApplicationEvent(new Test1Event("hello world!"));
		}
	}

}
