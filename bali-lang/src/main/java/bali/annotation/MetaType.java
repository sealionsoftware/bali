package bali.annotation;

import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

/**
 * User: Richard
 * Date: 27/08/13
 */
@Target(TYPE)
public @interface MetaType {

	public Kind value();

}
