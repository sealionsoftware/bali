package bali;

/**
 * User: Richard
 * Date: 02/05/13
 */
public enum Boolean {

	FALSE(false), TRUE(true);

	final boolean value;

	Boolean(boolean value) {
		this.value = value;
	}

	public Boolean not(){
		return forPrimitive(!value);
	}

	public Boolean and(Boolean operand){
		return forPrimitive(value && operand.value);
	}

	public Boolean or(Boolean operand){
		return forPrimitive(value || operand.value);
	}

	public Boolean xor(Boolean operand){
		return forPrimitive((value || operand.value) && !(value && operand.value));
	}

	public Boolean equals(Boolean operand){
		return forPrimitive(value == operand.value);
	}

	static Boolean forPrimitive(boolean in){
		return in ? Boolean.TRUE : Boolean.FALSE;
	}

}
