package bali;

/**
 * User: Richard
 * Date: 26/06/13
 */
public class NullReferenceException extends RuntimeException {

	private String method;

	public NullReferenceException(java.lang.String method) {
		this.method = new String(method.toCharArray());
	}

	public String getMethod() {
		return method;
	}
}
