package br.com.ape.selenium.auth;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

public class AuthenticationRule implements MethodRule {

	private final String username;
	private final String passwd;

	public AuthenticationRule(String username, String passwd) {
		super();
		this.username = username;
		this.passwd = passwd;
	}

	public static AuthenticationRule newLoginRule() {
		return new AuthenticationRule("", "");
	}

	public static AuthenticationRule defaultUserAndPasswd(String username, String passwd) {
		return new AuthenticationRule(username, passwd);
	}

	@Override
	public Statement apply(final Statement base, FrameworkMethod method, final Object target) {
		if( !(target instanceof AuthenticatedTest) ){
			return base;
		}
		final Authenticate login = getLoginAnnotation(method);
		if (login != null) {
			return new Statement() {
				@Override
				public void evaluate() throws Throwable {
					AuthenticatedTest test = (AuthenticatedTest) target;
					test.login(user(login), passwd(login));
					base.evaluate();
					test.logout();

				}
			};
		}
		return base;
	}

	private String user(Authenticate logged) {
		return "".equals(logged.username()) ? username : logged.username();
	}

	private String passwd(Authenticate logged) {
		return "".equals(logged.password()) ? passwd : logged.password();
	}

	private Authenticate getLoginAnnotation(FrameworkMethod method) {
		return method.getAnnotation(Authenticate.class);
	}

}
