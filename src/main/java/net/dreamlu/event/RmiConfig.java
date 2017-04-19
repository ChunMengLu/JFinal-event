package net.dreamlu.event;

import java.rmi.registry.Registry;

/**
 * Rmi配置抽象
 * 
 * @author L.cm
 *
 */
public abstract class RmiConfig {
	protected final int port;
	protected Registry registry;
	
	protected abstract boolean start();
	protected abstract boolean stop();
	
	public RmiConfig(int port) {
		this.port = port;
	}

	public int getPort() {
		return port;
	}

	public Registry getRegistry() {
		return registry;
	}
}