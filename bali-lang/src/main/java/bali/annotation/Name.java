package bali.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;

/**
 * User: Richard
 * Date: 09/09/13
 */
@Target(PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Name {
	public String value();
}
