package bali;

/**
 * User: Richard
 * Date: 02/05/13
 */
public class False implements Boolean {

	public static final Boolean FALSE = new False();

	private False(){}

	public Boolean not() {
		return True.TRUE;
	}

	// This is a stub - the bali compiler implements the real logic directly
	public Boolean and(Boolean operand) {
		return FALSE;
	}

	// This is a stub - the bali compiler implements the real logic directly
	public Boolean or(Boolean operand) {
		return operand;
	}

	public Boolean xor(Boolean that) {
		return that;
	}

	public Boolean equalTo(Boolean operand) {
		return this == operand ? True.TRUE : FALSE;
	}

}
