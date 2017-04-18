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
public class EventServiceImpl extends UnicastRemoteObject implements EventService {
	private static final long serialVersionUID = -1700474712741128882L;

	public EventServiceImpl() throws RemoteException {}

	@Override
	public void post(String tag, ApplicationEvent event) throws RemoteException {
		EventKit.post(tag, event);
	}

}
