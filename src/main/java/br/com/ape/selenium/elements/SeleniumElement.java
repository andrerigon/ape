package br.com.ape.selenium.elements;

import com.thoughtworks.selenium.Selenium;

import java.util.concurrent.TimeUnit;

public final class SeleniumElement {

	public static final String DISPLAY_NONE_SCRIPT = "document.getElementById('%s').setStyle('display: none')";
	private final String element;
	private final Selenium selenium;

	public SeleniumElement(String element, Selenium selenium) {
		this.element = element;
		this.selenium = selenium;
	}

	public SeleniumElement setValue(String value) {
		selenium.type(element, value);
		return this;
	}

	public boolean isPresentUntil(long millis) throws InterruptedException {
		for (int i = 0; i < TimeUnit.MILLISECONDS.toSeconds(millis); i++) {
			if (selenium.isElementPresent(element)) {
				return true;
			}
			Thread.sleep(TimeUnit.SECONDS.toMillis(1));
		}
		return false;
	}

	public SeleniumElement click() {
		selenium.click(element);
		return this;
	}

	public SeleniumElement waitToLoad(long millis) {
		selenium.waitForPageToLoad(String.valueOf(millis));
		return this;
	}

	public String text() {
		return selenium.getText(element);
	}

	public String value() {
		return selenium.getValue(element);
	}

	public SeleniumElement selectByLabel(String label) {
		this.lowlevelSelect(label, SelectType.LABEL);
		return this;
	}

	protected SeleniumElement lowlevelSelect(String value, SelectType type) {
		selenium.select(element, type.optionLocator(value));
		return this;
	}

	public SeleniumElement displayNone() {
		selenium.runScript(String.format(DISPLAY_NONE_SCRIPT, element));
		return this;
	}

	public boolean notPresentUntil(long millis) throws InterruptedException {
		for (int i = 0; i < TimeUnit.MILLISECONDS.toSeconds(millis); i++) {
			if (!selenium.isElementPresent(element)) {
				return true;
			}
			Thread.sleep(TimeUnit.SECONDS.toMillis(1));
		}
		return false;
	}

	private enum SelectType{
		LABEL,
		VALUE;

		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}

		public String optionLocator(String value) {
			return String.format("%s=%s", toString(), value);
		};
	}
}




