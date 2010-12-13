package br.com.ape.selenium.server;

import org.openqa.selenium.server.SeleniumServer;

public class SeleniumRCServer {

	private final SeleniumServer server;

	public SeleniumRCServer(SeleniumServer server) {
		super();
		this.server = server;
	}

	public void start(int port) throws Exception {
		if (isStarted()) {
			return;
		}
		server.getConfiguration().setPort(port);
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
