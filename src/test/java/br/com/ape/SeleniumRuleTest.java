package br.com.ape;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import br.com.ape.selenium.SeleniumTestCase;
import br.com.ape.selenium.config.SeleniumDriverConfig;
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
		final SeleniumStatement statement = new SeleniumStatement(test, base, mock(FrameworkMethod.class));
		doThrow(new RuntimeException()).when(base).evaluate();

		try {
			statement.evaluate();
		} catch (Throwable e) {
		}
		verify(test, timeout(1)).takeScreenshot(anyString());
	}

	@Test
	public void shouldStartSeleniumTestCase() throws Throwable {
		final SeleniumTestCase test = mock(SeleniumTestCase.class);
		final SeleniumStatement statement = new SeleniumStatement(test, mock(Statement.class),
				mock(FrameworkMethod.class));

		when(test.useEmbbebedSeleniumRC()).thenReturn(false);
		statement.evaluate();

		verify(test, times(1)).start();
	}
}
