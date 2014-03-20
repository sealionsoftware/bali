package bali.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.CONSTRUCTOR;

/**
 * User: Richard
 * Date: 20/03/14
 */
@Target(CONSTRUCTOR)
@Retention(RetentionPolicy.RUNTIME)
public @interface Parameters {
}
