package br.com.ape.selenium.server.rule;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import br.com.ape.selenium.SeleniumTestCase;
import br.com.ape.selenium.server.SeleniumRCServer;

public class SeleniumStatement extends Statement {

	private final Log log = LogFactory.getLog(getClass());

	private final SeleniumTestCase testCase;
	private final Statement orginalStatement;
	private final FrameworkMethod method;

	private final SeleniumRCServer server;

	public SeleniumStatement(SeleniumTestCase testCase, Statement orginalStatement, FrameworkMethod method,
			SeleniumRCServer server) {
		this.testCase = testCase;
		this.orginalStatement = orginalStatement;
		this.method = method;
		this.server = server;
	}

	public void evaluate() throws Throwable {
		try {
			startSeleniumRCIfNeeded();
			testCase.start();
			orginalStatement.evaluate();
		} catch (Throwable e) {
			takeScreenshotOnError();
			throw e;
		} finally {
			testCase.stop();
		}
	}

	private void startSeleniumRCIfNeeded() throws Exception {
		if (testCase.useEmbbebedSeleniumRC() && server != null && !server.isStarted()) {
			server.start(testCase.config().port());
		}
	}

	private void takeScreenshotOnError() throws Throwable {
		try {
			testCase.takeScreenshot(method.getName());
		} catch (Exception e2) {
			log.warn("Não foi possível capturar o screenshot");
		}
	}

}
