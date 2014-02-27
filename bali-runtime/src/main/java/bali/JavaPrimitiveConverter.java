package bali;

import bali.number.NumberFactory;

/**
 * User: Richard
 * Date: 26/02/14
 */
public class JavaPrimitiveConverter implements PrimitiveConverter {

	public Integer from(int in) {
		return NumberFactory.NUMBER_FACTORY.forInt(in);
	}

	public int from(Integer in) {
		return NumberFactory.NUMBER_FACTORY.valueOf(in);
	}

	public Boolean from(boolean in) {
		return in ? IdentityBoolean.TRUE : IdentityBoolean.FALSE;
	}

	public boolean from(Boolean in) {
		return in == IdentityBoolean.TRUE;
	}

	public String from(char[] in) {
		return new CharArrayString(in);
	}
}
