package br.com.ape.selenium.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Inherited
public @interface SeleniumActionDelay {

	public long delay () default 0;
	
	public TimeUnit timeUnit () default TimeUnit.MILLISECONDS;
}
