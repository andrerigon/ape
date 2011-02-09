package br.com.ape.selenium;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import br.com.ape.selenium.SeleniumBaseTest;
import br.com.ape.selenium.config.SeleniumDriverConfig;
import br.com.ape.selenium.server.rule.SeleniumRule;

public class SeleniumBaseTestTest {


	private static final String BASE_URL = "baseURL";
	private static final String BROWSER= "IE. hehehehe";
	private static final String HOST = "localhost";
	private static final int PORT = 0;

	@Test(expected = RuntimeException.class)
	public void noSeleniumDriverConfigDefined() {
		new SeleniumBaseTest() {
		};
	}

	@Test
	public void shouldCreateTestWithSeleniumDriveConfigDefined() {
		SeleniumDriverConfig config = sampleSeleniumBaseTest().config();
		assertThat( BASE_URL , equalTo( config.baseURL() ));
		assertThat( BROWSER , equalTo( config.browser() ));
		assertThat( HOST , equalTo( config.host() ));
		assertThat( PORT , equalTo( config.port() ));
	}

	@Test
	public void shouldStartSeleniumTest(){

	}

	private SeleniumBaseTest sampleSeleniumBaseTest() {
		@SeleniumDriverConfig(baseURL=BASE_URL, browser= BROWSER, host=HOST, port=PORT)
		class SampleTest extends SeleniumBaseTest {
		}
		;
		final SampleTest sampleTest = new SampleTest();
		sampleTest.seleniumRule = mock(SeleniumRule.class);
		return sampleTest;
	}
}
