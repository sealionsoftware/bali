package bali.annotation;

import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;

/**
 * User: Richard
 * Date: 09/09/13
 */
@Target(PARAMETER)
public @interface Name {
	public String value();
}
