package br.com.ape.selenium.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Inherited
public @interface TimeoutConfig {

	public long waitTimeoutInMillis () default 30000;
	
	public long presenceTimeoutInMillis () default 30000;
	
	public long visibilityTimeoutInMillis () default 20000;
	
}
