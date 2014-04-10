package bali;

import java.lang.String;

import static bali.Primitive.convert;

/**
 * User: Richard
 * Date: 07/02/14
 */
public class BaliThrowable extends RuntimeException {

	public final Exception thrown;

	public BaliThrowable(String thrown) {
		this.thrown = new Exception(convert(thrown));
	}

	public BaliThrowable(Exception thrown) {
		this.thrown = thrown;
	}

	public String getMessage() {
		return convert(thrown.message);
	}
}
