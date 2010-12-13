package br.com.ape;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import br.com.ape.selenium.SeleniumTestCase;
import br.com.ape.selenium.config.SeleniumDriverConfig;
import br.com.ape.selenium.server.SeleniumRCServer;
import br.com.ape.selenium.server.rule.SeleniumRule;
import br.com.ape.selenium.server.rule.SeleniumStatement;

public class SeleniumRuleTest {

	@Test
	public void shouldNotModifieStatementIfTestIsNotASelenium() throws Exception {
		final SeleniumRule rule = new SeleniumRule();
		final Statement stmt = mock(Statement.class);
		assertThat(rule.apply(stmt, null, new Object()), equalTo(stmt));
	}

	@Test
	public void shouldReturnAModifiedStatementForSeleniumTestCase() throws Exception {
		final SeleniumRule rule = new SeleniumRule();
		final Statement stmt = mock(Statement.class);
		assertThat(rule.apply(stmt, null, mock(SeleniumTestCase.class)), not(equalTo(stmt)));
	}

	@Test
	public void shouldTakeScreenshotOnError() throws Throwable {
		final Statement base = mock(Statement.class);
		final SeleniumTestCase test = mock(SeleniumTestCase.class);
		final SeleniumStatement statement = new SeleniumStatement(test, base, mock(FrameworkMethod.class),
				mock(SeleniumRCServer.class));
		doThrow(new RuntimeException()).when(base).evaluate();

		try {
			statement.evaluate();
		} catch (Throwable e) {
		}
		verify(test, timeout(1)).takeScreenshot(anyString());
	}

	@Test
	public void shouldStartSeleniumRCIfRequired() throws Throwable {
		final SeleniumTestCase test = mock(SeleniumTestCase.class);
		final SeleniumRCServer server = mock(SeleniumRCServer.class);
		final SeleniumDriverConfig config = mock(SeleniumDriverConfig.class);
		final SeleniumStatement statement = new SeleniumStatement(test, mock(Statement.class),
				mock(FrameworkMethod.class), server);

		when(test.useEmbbebedSeleniumRC()).thenReturn(true);
		when(test.config()).thenReturn(config);
		when(config.port()).thenReturn(123);
		statement.evaluate();

		verify(server, times(1)).start(123);
	}

	@Test
	public void shouldNotStartSeleniumRCIfRequired() throws Throwable {
		final SeleniumTestCase test = mock(SeleniumTestCase.class);
		final SeleniumRCServer server = mock(SeleniumRCServer.class);
		final SeleniumDriverConfig config = mock(SeleniumDriverConfig.class);
		final SeleniumStatement statement = new SeleniumStatement(test, mock(Statement.class),
				mock(FrameworkMethod.class), server);

		when(test.useEmbbebedSeleniumRC()).thenReturn(false);
		when(test.config()).thenReturn(config);
		when(config.port()).thenReturn(123);
		statement.evaluate();

		verify(server, never()).start(anyInt());
	}

	@Test
	public void shouldStartSeleniumTestCase() throws Throwable {
		final SeleniumTestCase test = mock(SeleniumTestCase.class);
		final SeleniumStatement statement = new SeleniumStatement(test, mock(Statement.class),
				mock(FrameworkMethod.class), mock(SeleniumRCServer.class));

		when(test.useEmbbebedSeleniumRC()).thenReturn(false);
		statement.evaluate();

		verify(test, times(1)).start();
	}
}
