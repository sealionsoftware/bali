package bali;

import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.annotation.Name;
import bali.annotation.Parameters;

/**
 * User: Richard
 * Date: 07/07/13
 */
@MetaType(Kind.BEAN)
public class Exception {

	public String message;

	@Parameters
	public Exception(@Name("message") String message) {
		this.message = message;
	}

}
