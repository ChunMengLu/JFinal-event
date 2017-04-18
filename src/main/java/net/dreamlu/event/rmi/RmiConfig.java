package net.dreamlu.event.rmi;

import java.rmi.registry.Registry;

public abstract class RmiConfig {
	private final int port;
	protected Registry registry;
	
	public abstract boolean start();
	
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