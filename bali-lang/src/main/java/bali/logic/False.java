package bali.logic;

import bali.Boolean;


/**
 * User: Richard
 * Date: 02/05/13
 */
public final class False implements Boolean {

	public static final Boolean VALUE = new False();

	private False(){}

	public Boolean not() {
		return TRUE;
	}

	public Boolean and(Boolean operand) {
		return this;
	}

	public Boolean or(Boolean operand) {
		return operand;
	}

	public Boolean xor(Boolean that) {
		return that;
	}

	public bali.Boolean equalTo(Boolean operand) {
		return this == operand ? TRUE : this;
	}

	public Boolean notEqualTo(Boolean operand) {
		return equalTo(operand).not();
	}

	public java.lang.String toString(){
		return "FALSE";
	}

}
