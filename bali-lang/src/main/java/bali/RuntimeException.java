package bali;


/**
 * User: Richard
 * Date: 23/10/13
 */
public class RuntimeException implements Exception {

	private String message;

	public RuntimeException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
