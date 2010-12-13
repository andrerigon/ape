package br.com.ape.selenium.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Inherited
public @interface SeleniumDriverConfig {

	public String host() default "localhost";

	public int port() default 7986;

	public String browser() default "*chrome";

	public String baseURL() default "http://localhost:8080";

	public boolean useEmbbebedSeleniumRC() default false;
}
