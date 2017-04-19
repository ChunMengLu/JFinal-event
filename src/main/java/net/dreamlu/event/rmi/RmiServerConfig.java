package net.dreamlu.event.rmi;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

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
			throw new RuntimeException(e);
		} catch (AlreadyBoundException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	protected boolean stop() {
		try {
			String[] services = registry.list();
			for (String service : services) {
				Remote remote = registry.lookup(service);
				UnicastRemoteObject.unexportObject(remote, true);
				registry.unbind(service);
			}
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
