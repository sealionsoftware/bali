package bali;


import bali.annotation.MetaType;
import bali.annotation.MetaTypes;

/**
 * User: Richard
 * Date: 23/10/13
 */
@MetaType(MetaTypes.CLASS)
public class RuntimeException implements Exception {

	private String message;

	public RuntimeException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
