package bali.annotation;

import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

/**
 * User: Richard
 * Date: 27/06/13
 */
@Target(METHOD)
public @interface Operator {
	java.lang.String value();
}
