package bali.annotation;

import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * User: Richard
 * Date: 21/01/14
 */
@Target({FIELD, METHOD, PARAMETER, LOCAL_VARIABLE})
public @interface ThreadSafe {
}
