package bali;


import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.annotation.Name;
import bali.annotation.Parameters;

/**
 * User: Richard
 * Date: 23/10/13
 */
@MetaType(Kind.OBJECT)
public class RuntimeException implements Exception {

	private String message;

	@Parameters
	public RuntimeException(@Name("name") String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
