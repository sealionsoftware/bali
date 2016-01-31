package bali.logic;

import bali.Logic;

public final class False implements Logic {

	public static final Logic VALUE = new False();

	private False(){}

	public Logic not() {
		return TRUE;
	}

	public Logic and(Logic operand) {
		return this;
	}

	public Logic or(Logic operand) {
		return operand;
	}

	public Logic xor(Logic that) {
		return that;
	}

	public Logic equalTo(Logic operand) {
		return this == operand ? TRUE : this;
	}

	public Logic notEqualTo(Logic operand) {
		return equalTo(operand).not();
	}

	public java.lang.String toString(){
		return "FALSE";
	}

}
