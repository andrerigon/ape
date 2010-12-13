package br.com.ape.selenium.server;

import org.openqa.selenium.server.RemoteControlConfiguration;
import org.openqa.selenium.server.SeleniumServer;

public enum SeleniumRCServer {

	INSTANCE;

	private SeleniumServer server;

	public void start() throws Exception {
		if (isStarted()) {
			return;
		}
		RemoteControlConfiguration config = new RemoteControlConfiguration();
		config.setPort(7986);
		server = new SeleniumServer(config);
		server.start();
	}

	public void stop() {
		if (!isStarted()) {
			return;
		}
		server.stop();
	}

	public boolean isStarted() {
		return server != null && server.getServer().isStarted();
	}
}
