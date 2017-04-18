package net.dreamlu.event.rmi;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import net.dreamlu.event.service.EventService;
import net.dreamlu.event.service.EventServiceImpl;
import net.dreamlu.event.service.RmiService;
import net.dreamlu.utils.BeanUtil;

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
		RmiService rmiService = EventServiceImpl.class.getAnnotation(RmiService.class);
		Class<? extends Remote> clazz = rmiService.value();
		String name = clazz.getSimpleName();
		EventService eventService = BeanUtil.newInstance(EventServiceImpl.class);
		registry.bind(name, eventService);
	}
}
