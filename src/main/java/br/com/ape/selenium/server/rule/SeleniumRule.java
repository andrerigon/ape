package br.com.ape.selenium.server.rule;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import br.com.ape.selenium.SeleniumTestCase;

public class SeleniumRule implements MethodRule {

	public Statement apply(final Statement statement, final FrameworkMethod frameworkMethod, final Object testCase) {
		if (testCase instanceof SeleniumTestCase) {
			return new SeleniumStatement((SeleniumTestCase) testCase, statement, frameworkMethod);
		}
		return statement;
	}

}
