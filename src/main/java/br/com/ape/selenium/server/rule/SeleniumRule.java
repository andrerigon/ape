package br.com.ape.selenium.server.rule;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.openqa.selenium.server.SeleniumServer;

import br.com.ape.selenium.SeleniumTestCase;
import br.com.ape.selenium.server.SeleniumRCServer;

public class SeleniumRule implements MethodRule {

	private final SeleniumRCServer seleniumRCServer;

	public SeleniumRule() throws Exception {
		this(new SeleniumRCServer(new SeleniumServer()));
	}

	public static SeleniumRule defaultRule() {
		try {
			return new SeleniumRule(new SeleniumRCServer(new SeleniumServer()));
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	public SeleniumRule(SeleniumRCServer seleniumRCServer) {
		this.seleniumRCServer = seleniumRCServer;
	}

	public Statement apply(final Statement statement, final FrameworkMethod frameworkMethod, final Object testCase) {
		if (testCase instanceof SeleniumTestCase) {
			return new SeleniumStatement((SeleniumTestCase) testCase, statement, frameworkMethod, seleniumRCServer);
		}
		return statement;
	}

}
