package bali;

/**
 * User: Richard
 * Date: 02/05/13
 */
public class True implements Boolean {

	public static final Boolean TRUE = new True();

	private True(){}

	public Boolean not() {
		return False.FALSE;
	}

	// This is a stub - the bali compiler implements the real logic directly
	public Boolean and(Boolean operand) {
		return operand;
	}

	// This is a stub - the bali compiler implements the real logic directly
	public Boolean or(Boolean operand) {
		return TRUE;
	}

	public Boolean xor(Boolean that) {
		return that.not();
	}

	public Boolean equalTo(Boolean operand) {
		return this == operand ? TRUE : False.FALSE;
	}

}
