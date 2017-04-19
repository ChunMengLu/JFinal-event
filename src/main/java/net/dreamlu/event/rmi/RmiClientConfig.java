package net.dreamlu.event.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import net.dreamlu.event.RmiConfig;

/**
 * Rmi客户端配置
 * @author L.cm
 *
 */
public class RmiClientConfig extends RmiConfig {
	private final String host;

	public RmiClientConfig(int port, String host) {
		super(port);
		this.host = host;
	}
	
	public String getHost() {
		return host;
	}

	@Override
	protected boolean start() {
		try {
			registry = LocateRegistry.getRegistry(host, port);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	protected boolean stop() {
		return false;
	}
}
