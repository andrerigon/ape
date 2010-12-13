package br.com.ape;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.ape.selenium.elements.SeleniumElement;

import com.thoughtworks.selenium.Selenium;

@RunWith(MockitoJUnitRunner.class)
public class SeleniumElementTest {

	@Mock
	Selenium selenium;

	final String name = String.valueOf(System.nanoTime());

	@Test
	public void setValueOnElement() {
		SeleniumElement element = seleniumElement(name);
		element.setValue("qq");
		hasOneExecution(selenium).type(name, "qq");
	}

	@Test
	public void testElementPresent() throws InterruptedException {
		when(selenium.isElementPresent(name)).thenReturn(true);
		assertTrue(seleniumElement(name).isPresentUntil(1000));
		hasOneExecution(selenium).isElementPresent(anyString());
	}

	@Test
	public void testElementNotPresent() throws InterruptedException {
		final long waitTimeInMillis = 2000;
		when(selenium.isElementPresent(name)).thenReturn(false);
		assertTrue(seleniumElement(name).notPresentUntil(waitTimeInMillis));
		hasOneExecution(selenium).isElementPresent(anyString());
	}

	@Test
	public void shouldClick() {
		seleniumElement(name).click();
		hasOneExecution(selenium).click(name);
	}

	@Test
	public void shoulWaitForPageToLoad() {
		seleniumElement(name).waitToLoad(12345);
		hasOneExecution(selenium).waitForPageToLoad(String.valueOf(12345));
	}

	@Test
	public void shouldReturnElementText() {
		when(selenium.getText(name)).thenReturn("myText");
		assertThat(seleniumElement(name).text(), equalTo("myText"));
		hasOneExecution(selenium).getText(name);
	}

	@Test
	public void shouldReturnElementValue() {
		when(selenium.getValue(name)).thenReturn("myValue");
		assertThat(seleniumElement(name).value(), equalTo("myValue"));
		hasOneExecution(selenium).getValue(name);
	}

	@Test
	public void shouldChangeElementoToDisplayNone() {
		seleniumElement(name).displayNone();
		hasOneExecution(selenium).runScript(String.format(SeleniumElement.DISPLAY_NONE_SCRIPT, name));
	}

	@Test
	public void shouldSelectCorrectElementByLabel() {
		seleniumElement(name).selectByLabel(name);
		hasOneExecution(selenium).select(name, "label=" + name);
	}

	private <T> T hasOneExecution(T mock) {
		return verify(mock, times(1));
	}

	private SeleniumElement seleniumElement(String locator) {
		SeleniumElement element = new SeleniumElement(locator, selenium);
		return element;
	}
}
