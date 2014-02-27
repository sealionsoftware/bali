package bali.annotation;

import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

/**
 * User: Richard
 * Date: 10/11/13
 */
@Target({FIELD, METHOD})
public @interface Hidden {
}
