package bali;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

/**
 * User: Richard
 * Date: 27/06/13
 */
@Target(METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface Operator {
	java.lang.String value();
}
