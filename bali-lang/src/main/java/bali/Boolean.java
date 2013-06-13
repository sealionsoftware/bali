package bali;

/**
 * User: Richard
 * Date: 02/05/13
 */
public enum Boolean implements Value<Boolean> {

	FALSE, TRUE;

	public Boolean not() {
		return this == TRUE ? FALSE : TRUE;
	}

	public Boolean and(Boolean operand) {
		if (this == FALSE){
			return FALSE;
		} else if (operand == FALSE) {
			return FALSE;
		}
		return TRUE;
	}

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
