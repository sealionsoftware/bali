package bali.logic;

import bali.Logic;

public final class True implements Logic {

	public static final Logic VALUE = new True();

	private True(){}

	public Logic not() {
		return FALSE;
	}

	public Logic and(Logic operand) {
		return operand;
	}

	public Logic or(Logic operand) {
		return this;
	}

	public Logic xor(Logic that) {
		return that.not();
	}

	public Logic equalTo(Logic operand) {
		return this == operand ? this : FALSE;
	}

	public Logic notEqualTo(Logic operand) {
		return equalTo(operand).not();
	}

	public java.lang.String toString(){
		return "TRUE";
	}

}
