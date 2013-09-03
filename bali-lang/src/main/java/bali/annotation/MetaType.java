package bali.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * User: Richard
 * Date: 27/08/13
 */
@Target({PARAMETER, METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MetaType {

	public  java.lang.String value();

}
