package br.com.ape;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.jetty.jetty.Server;
import org.openqa.selenium.server.RemoteControlConfiguration;
import org.openqa.selenium.server.SeleniumServer;

import br.com.ape.selenium.server.SeleniumRCServer;

@RunWith(MockitoJUnitRunner.class)
public class SeleniumRCServerTest {

	@Mock
	SeleniumServer server;

	@Test
	public void shouldStartServer() throws Exception {
		final RemoteControlConfiguration config = mock(RemoteControlConfiguration.class);
		final Server internalServer = mock(Server.class);

		when(server.getConfiguration()).thenReturn(config);
		when(server.getServer()).thenReturn(internalServer);
		when(internalServer.isStarted()).thenReturn(false);

		new SeleniumRCServer(server).start(12345);

		verify(config, times(1)).setPort(12345);
		verify(server, times(1)).start();

	}

	@Test
	public void shouldNotStartAlreadyStartedServer() throws Exception {
		final Server internalServer = mock(Server.class);

		when(server.getServer()).thenReturn(internalServer);
		when(internalServer.isStarted()).thenReturn(true);

		new SeleniumRCServer(server).start(12345);

		verify(internalServer, never()).start();
	}

	@Test
	public void souldStopServer() throws Exception {
		final Server internalServer = mock(Server.class);

		when(server.getServer()).thenReturn(internalServer);
		when(internalServer.isStarted()).thenReturn(true);

		new SeleniumRCServer(server).stop();

		verify(server, times(1)).stop();
	}

	@Test
	public void souldNotStopServer() throws Exception {
		final Server internalServer = mock(Server.class);

		when(server.getServer()).thenReturn(internalServer);
		when(internalServer.isStarted()).thenReturn(false);

		new SeleniumRCServer(server).stop();

		verify(server, never()).stop();
	}
}
