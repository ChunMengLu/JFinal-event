package net.dreamlu.event.rmi;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import net.dreamlu.event.service.EventService;

/**
 * Rmi客户端配置
 * @author L.cm
 *
 */
public class RmiClientConfig extends RmiConfig {
	private final String host;

	public RmiClientConfig(int port, String host) throws RemoteException {
		super(port);
		this.host = host;
		super.registry = LocateRegistry.getRegistry(host, port);
	}
	
	public EventService getEventService() throws AccessException, RemoteException, NotBoundException {
		Class<? extends Remote> clazz = EventService.class;
		return (EventService) registry.lookup(clazz.getSimpleName());
	}
	
	public String getHost() {
		return host;
	}
}
