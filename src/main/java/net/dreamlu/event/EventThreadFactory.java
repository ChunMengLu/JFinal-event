package net.dreamlu.event;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import com.jfinal.log.Log;

/**
 * 消息线程池
 * 
 * @author L.cm
 *
 */
public class EventThreadFactory implements ThreadFactory {
	private static final AtomicInteger poolNumber = new AtomicInteger(1);
	private final ThreadGroup group;
	private final AtomicInteger threadNumber = new AtomicInteger(1);
	private final String namePrefix;

	public EventThreadFactory() {
		SecurityManager s = System.getSecurityManager();
		group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
		namePrefix = "jfinal-event-pool-" + poolNumber.getAndIncrement() + "-thread-";
	}

	public Thread newThread(Runnable r) {
		Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
		if (t.isDaemon())
			t.setDaemon(false);
		if (t.getPriority() != Thread.NORM_PRIORITY)
			t.setPriority(Thread.NORM_PRIORITY);
		t.setUncaughtExceptionHandler(new ThreadUncaughtExceptionHandler());
		return t;
	}

	static class ThreadUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
		private static Log log = Log.getLog(ThreadUncaughtExceptionHandler.class);

		public void uncaughtException(Thread t, Throwable e) {
			log.error(e.getMessage(), e);
		}
	}

}
