package net.dreamlu.event.rmi;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import net.dreamlu.event.service.EventService;
import net.dreamlu.event.service.EventServiceImpl;

/**
 * Rmi服务端配置
 * @author lcm
 *
 */
public class RmiServerConfig extends RmiConfig {
	
	public RmiServerConfig(int port) throws RemoteException {
		super(port);
		super.registry = LocateRegistry.createRegistry(port);
	}
	
	public void bindEventService() throws AccessException, RemoteException, AlreadyBoundException {
		String name = EventService.class.getSimpleName();
		EventService eventService = new EventServiceImpl();
		registry.bind(name, eventService);
	}
	
	public void unbindEventService() throws AccessException, RemoteException, NotBoundException {
		registry.unbind(EventService.class.getSimpleName());
	}
}
