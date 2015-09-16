package bali.logic;

import bali.Boolean;

/**
 * User: Richard
 * Date: 02/05/13
 */
public class True implements Boolean {

	public static final Boolean VALUE = new True();

	private True(){}

	public Boolean not() {
		return FALSE;
	}

	public Boolean and(Boolean operand) {
		return operand;
	}

	public Boolean or(Boolean operand) {
		return this;
	}

	public Boolean xor(Boolean that) {
		return that.not();
	}

	public Boolean equalTo(Boolean operand) {
		return this == operand ? this : FALSE;
	}

	public Boolean notEqualTo(Boolean operand) {
		return equalTo(operand).not();
	}

	public java.lang.String toString(){
		return "TRUE";
	}

}
