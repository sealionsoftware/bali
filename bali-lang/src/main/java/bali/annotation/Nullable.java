package bali.annotation;

import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * User: Richard
 * Date: 09/09/13
 */
@Target({FIELD, METHOD, PARAMETER, LOCAL_VARIABLE})
public @interface Nullable {
}
