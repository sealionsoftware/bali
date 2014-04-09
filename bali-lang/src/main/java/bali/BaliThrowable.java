package bali;

import java.lang.String;

/**
 * User: Richard
 * Date: 07/02/14
 */
public class BaliThrowable extends Throwable {

	public final Exception thrown;

	public BaliThrowable(Exception thrown) {
		this.thrown = thrown;
	}

	public String getMessage() {
		return Primitive.convert(thrown.message);
	}
}
