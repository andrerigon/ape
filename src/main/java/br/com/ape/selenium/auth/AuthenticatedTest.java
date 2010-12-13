package br.com.ape.selenium.auth;

public interface AuthenticatedTest {

	void login(String username, String passwd);

	void logout();

}