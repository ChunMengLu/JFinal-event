package net.dreamlu.event.rmi;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import net.dreamlu.event.RmiConfig;
import net.dreamlu.event.service.EventService;
import net.dreamlu.event.service.EventServiceImpl;

/**
 * Rmi服务端配置
 * @author lcm
 *
 */
public class RmiServerConfig extends RmiConfig {
	
	public RmiServerConfig(int port) {
		super(port);
	}
	
	@Override
	protected boolean start() {
		try {
			registry = LocateRegistry.createRegistry(port);
			String name = EventService.class.getSimpleName();
			EventService eventService = new EventServiceImpl();
			registry.bind(name, eventService);
			return true;
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	protected boolean stop() {
		try {
			registry.unbind(EventService.class.getSimpleName());
			return true;
		} catch (AccessException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		return false;
	}
}
