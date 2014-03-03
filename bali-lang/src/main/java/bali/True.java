package bali;

import bali.annotation.Name;

/**
 * User: Richard
 * Date: 02/05/13
 */
public class True implements Boolean {

	static final Boolean VALUE = new True();

	private True(){}

	public Boolean not() {
		return FALSE;
	}

	// This is a stub - the bali compiler implements the real logic directly to avoid
	// evaluation of the operand
	public Boolean and(Boolean operand) {
		return operand;
	}

	// This is a stub - the bali compiler implements the real logic directly to avoid
	// evaluation of the operand
	public Boolean or(Boolean operand) {
		return this;
	}

	public Boolean xor(Boolean that) {
		return that.not();
	}

	public Boolean equalTo(Boolean operand) {
		return this == operand ? this : FALSE;
	}

	public Boolean notEqualTo(@Name("operand") Boolean operand) {
		return equalTo(operand).not();
	}

	public java.lang.String toString(){
		return "TRUE";
	}

}
