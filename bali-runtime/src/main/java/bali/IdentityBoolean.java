package bali;

import bali.annotation.Kind;
import bali.annotation.MetaType;

/**
 * User: Richard
 * Date: 02/05/13
 */
@MetaType(Kind.OBJECT)
public enum IdentityBoolean implements Boolean {

	FALSE, TRUE;

	public Boolean not() {
		return this == TRUE ? FALSE : TRUE;
	}

	// This is a stub - the bali compiler implements the real logic directly
	public Boolean and(Boolean operand) {
		if (this == FALSE){
			return FALSE;
		} else if (operand == FALSE) {
			return FALSE;
		}
		return TRUE;
	}

	// This is a stub - the bali compiler implements the real logic directly
	public Boolean or(Boolean operand) {
		if (this == FALSE && operand == FALSE){
			return FALSE;
		}
		return TRUE;
	}

	public Boolean xor(Boolean that) {
		return this.or(that).and(this.and(that).not());
	}

	public Boolean equalTo(Boolean operand) {
		return this == operand ? TRUE : FALSE;
	}

}
