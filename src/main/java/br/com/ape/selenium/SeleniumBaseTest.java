package br.com.ape.selenium;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Rule;

import br.com.ape.selenium.config.ErrorScreenshotPath;
import br.com.ape.selenium.config.MaximizedBrowserWindow;
import br.com.ape.selenium.config.SeleniumActionDelay;
import br.com.ape.selenium.config.SeleniumDriverConfig;
import br.com.ape.selenium.config.TimeoutConfig;
import br.com.ape.selenium.elements.SeleniumElement;
import br.com.ape.selenium.server.rule.SeleniumRule;
import br.com.ape.selenium.util.AnnotationDefault;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;

public abstract class SeleniumBaseTest implements SeleniumTestCase {

	private final Selenium selenium;

	private final SeleniumDriverConfig config;

	private final TimeoutConfig timeout;

	private final ErrorScreenshotPath screenshotDir;

	private final Map<String, SeleniumElement> map = new WeakHashMap<String, SeleniumElement>();

	public SeleniumBaseTest() {
		config = getClass().getAnnotation(SeleniumDriverConfig.class);
		if (config == null) {
			throw new RuntimeException(
					"Your test doesn't have a selenium drive configuration. Please annotate your test with @SeleniumDriverConfig");
		}
		selenium = createSeleniumDriver();
		timeout = getConfig(TimeoutConfig.class);
		screenshotDir = getConfig(ErrorScreenshotPath.class);
	}

	protected <T extends Annotation> T getConfig(Class<T> clazz) {
		final T cfg = getClass().getAnnotation(clazz);
		return cfg == null ? AnnotationDefault.of(clazz) : cfg;
	}

	@Rule
	public SeleniumRule seleniumRule = new SeleniumRule();

	@Override
	public boolean useEmbbebedSeleniumRC() {
		return config.useEmbbebedSeleniumRC();
	}

	public void start() {
		selenium.start();
		defineWindowSize();
		defineDelay();
	}

	public SeleniumDriverConfig config() {
		return config;
	}

	private Selenium createSeleniumDriver() {
		return new DefaultSelenium(config.host(), config.port(), config.browser(), config.baseURL());
	}

	private void defineDelay() {
		final SeleniumActionDelay delay = getClass().getAnnotation(SeleniumActionDelay.class);
		if (delay != null) {
			selenium.setSpeed(String.valueOf(delay.timeUnit().toMillis(delay.delay())));
		}

	}

	private void defineWindowSize() {
		if (getClass().getAnnotation(MaximizedBrowserWindow.class) != null) {
			selenium.windowMaximize();
		}

	}

	public void stop() {
		selenium.stop();
	}

	protected SeleniumElement on(String element) {
		if (!map.containsKey(element)) {
			map.put(element, new SeleniumElement(element, selenium));
		}
		return map.get(element);
	}

	protected void open(String page) {
		selenium.open(page);
	}

	protected boolean isTextPresent(String text) {
		return selenium.isTextPresent(text);
	}

	public void takeScreenshot(String methodName) {
		selenium.captureEntirePageScreenshot(
				String.format("%s/screenshot_%s_%s.png", screenshotDir.value(), getClass().getSimpleName(), methodName),
				"");
	}

	protected void assertPresent(String element) throws InterruptedException {
		assertThat(on(element).isPresentUntil(timeout.presenceTimeoutInMillis()), is(true));
	}

	protected void assertPresentAndClick(String element) throws InterruptedException {
		assertPresent(element);
		click(element);
	}

	protected void click(String element) {
		on(element).click();
	}

	protected void clickAndWait(String element) {
		on(element).click().waitToLoad(timeout.waitTimeoutInMillis());
	}

	protected void assertText(String element, String expected) {
		assertThat(on(element).text(), is(expected));
	}

	protected void assertText(String text) {
		assertThat(isTextPresent(text), is(true));
	}

	protected void assertVisible(String element) throws InterruptedException {
		for (int i = 0; i < TimeUnit.MILLISECONDS.toSeconds(timeout.visibilityTimeoutInMillis()); i++) {
			if (selenium.isVisible(element)) {
				return;
			}
			Thread.sleep(1000);
		}
		Assert.fail(String.format("Element %s is not visible", element));
	}

	protected void assertNotVisible(String element) throws InterruptedException {
		for (int i = 0; i < 20; i++) {
			if (!selenium.isVisible(element)) {
				return;
			}
			Thread.sleep(1000);
		}
		Assert.fail(String.format("Element %s is visible", element));
	}

	protected void assertNotPresent(String element) throws InterruptedException {
		assertThat(on(element).notPresentUntil(timeout.presenceTimeoutInMillis()), is(true));
	}

	protected void assertValue(String elem, String value) {
		assertThat(selenium.getValue(elem), is(value));
	}

	protected void assertChecked(String elem) {
		assertThat(selenium.isChecked(elem), is(true));
	}
	
	protected void assertNotChecked(String elem) {
		assertThat(selenium.isChecked(elem), is(false));
	}

	protected Selenium selenium() {
		return selenium;
	}
}
