package br.com.ape;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import br.com.ape.selenium.auth.Authenticate;
import br.com.ape.selenium.auth.AuthenticatedTest;
import br.com.ape.selenium.auth.AuthenticationRule;

public class AuthTest {

	private final AuthenticationRule authRule = AuthenticationRule.newLoginRule();

	@Test
	public void shouldProcessANonAuthenticatedTest() {
		final Statement statement = mock(Statement.class);
		assertThat(authRule.apply(statement, mock(FrameworkMethod.class), new Object()), equalTo(statement));
	}

	@Test
	public void shouldProcessAMethodWithoutAuthenticatedAnnotation() {
		final Statement statement = mock(Statement.class);
		assertThat(authRule.apply(statement, mock(FrameworkMethod.class), mock(AuthenticatedTest.class)),
				equalTo(statement));
	}

	@Test
	public void shouldProcessMethodWithAuthenticatedAnnotation() {
		final Authenticate auth = mock(Authenticate.class);
		final FrameworkMethod methodToBeTested = mock(FrameworkMethod.class);
		when(methodToBeTested.getAnnotation(Authenticate.class)).thenReturn(auth);

		final Statement statement = mock(Statement.class);
		assertThat(authRule.apply(statement, methodToBeTested, mock(AuthenticatedTest.class)), not(equalTo(statement)));
	}

	@Test
	public void shouldApplyRule() throws Throwable {
		final Authenticate auth = mock(Authenticate.class);
		final FrameworkMethod methodToBeTested = mock(FrameworkMethod.class);
		final AuthenticatedTest test = mock(AuthenticatedTest.class);
		
		when(methodToBeTested.getAnnotation(Authenticate.class)).thenReturn(auth);

		final Statement statement = mock(Statement.class);
		authRule.apply(statement, methodToBeTested, test).evaluate();

		verify(test, times(1)).login(anyString(), anyString());
		verify(test, times(1)).logout();
		verify(statement, times(1)).evaluate();
	}
}
