package bali;


import bali.annotation.Kind;
import bali.annotation.MetaType;

/**
 * User: Richard
 * Date: 23/10/13
 */
@MetaType(Kind.OBJECT)
public class RuntimeException implements Exception {

	private String message;

	public RuntimeException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
