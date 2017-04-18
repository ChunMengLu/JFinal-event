package net.dreamlu.event.service;

import java.rmi.Remote;

import net.dreamlu.event.core.ApplicationEvent;

/**
 * 基于Rmi的远程消息事件
 * @author L.cm
 *
 */
public interface EventService extends Remote {
	void post(final String tag, final ApplicationEvent event);
}
