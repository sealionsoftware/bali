package bali;

/**
 * User: Richard
 * Date: 02/05/13
 */
public class False implements Boolean {

	static final Boolean VALUE = new False();

	private False(){}

	public Boolean not() {
		return TRUE;
	}

	// This is a stub - the bali compiler implements the real logic directly to avoid
	// evaluation of the operand
	public Boolean and(Boolean operand) {
		return this;
	}

	// This is a stub - the bali compiler implements the real logic directly to avoid
	// evaluation of the operand
	public Boolean or(Boolean operand) {
		return operand;
	}

	public Boolean xor(Boolean that) {
		return that;
	}

	public Boolean equalTo(Boolean operand) {
		return this == operand ? TRUE : this;
	}

	public java.lang.String toString(){
		return "FALSE";
	}

}
