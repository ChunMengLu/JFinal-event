package net.dreamlu.event.service;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import net.dreamlu.event.EventKit;
import net.dreamlu.event.core.ApplicationEvent;

/**
 * 基于Rmi的远程消息事件
 * @author L.cm
 *
 */
@RmiService(EventService.class)
public class EventServiceImpl extends UnicastRemoteObject implements EventService {
	private static final long serialVersionUID = -1700474712741128882L;

	protected EventServiceImpl() throws RemoteException {}

	@Override
	public void post(String tag, ApplicationEvent event) {
		EventKit.post(tag, event);
	}

}
