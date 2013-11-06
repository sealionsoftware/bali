package bali;

import bali.annotation.MetaType;
import bali.annotation.MetaTypes;
import bali.annotation.Operator;

/**
 * User: Richard
 * Date: 02/05/13
 */
@MetaType(MetaTypes.CLASS)
public enum IdentityBoolean implements Boolean {

	FALSE, TRUE;

	@Operator("!")
	public Boolean not() {
		return this == TRUE ? FALSE : TRUE;
	}

	@Operator("&")
	public Boolean and(Boolean operand) {
		if (this == FALSE){
			return FALSE;
		} else if (operand == FALSE) {
			return FALSE;
		}
		return TRUE;
	}

	@Operator("|")
	public Boolean or(Boolean operand) {
		if (this == FALSE && operand == FALSE){
			return FALSE;
		}
		return TRUE;
	}

	@Operator("^")
	public Boolean xor(Boolean that) {
		return this.or(that).and(this.and(that).not());
	}

	@Operator("==")
	public Boolean equalTo(Boolean operand) {
		return this == operand ? TRUE : FALSE;
	}

}
