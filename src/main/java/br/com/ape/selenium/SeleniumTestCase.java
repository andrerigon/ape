package br.com.ape.selenium;

import br.com.ape.selenium.config.SeleniumDriverConfig;

public interface SeleniumTestCase {

	void start();

	void stop();

	void takeScreenshot( String methodName);
	
	boolean useEmbbebedSeleniumRC();

	SeleniumDriverConfig config();
	
}