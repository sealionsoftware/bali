package bali.annotation;

import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * Indicates the type argument to be the same type as the declaring site
 *
 * User: Richard
 * Date: 02/03/14
 */
@Target({FIELD, METHOD, PARAMETER})
public @interface SelfTyped {

}
