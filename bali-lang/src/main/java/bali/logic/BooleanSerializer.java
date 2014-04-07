package bali.logic;

import bali.Serializer;
import bali.Boolean;
import bali.String;
import bali.annotation.Name;

import static bali.Primitive.convert;

/**
 * User: Richard
 * Date: 01 Apr
 */
public class BooleanSerializer implements Serializer<Boolean> {

	private static final String TRUE = convert("true");
	private static final String FALSE = convert("false");

	public String format(@Name("in") Boolean in) {
		return convert(in) ? TRUE : FALSE;
	}

	public Boolean parse(@Name("serialization") String serialization) {
		if (convert(TRUE.equalTo(serialization))){
			return Boolean.TRUE;
		}
		if (convert(FALSE.equalTo(serialization))){
			return Boolean.FALSE;
		}
		throw new RuntimeException("Invalid Boolean serialization: " + convert(serialization));
	}
}
