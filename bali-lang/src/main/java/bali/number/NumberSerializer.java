package bali.number;

import bali.BaliThrowable;
import bali.CharArrayString;
import bali.Number;
import bali.Serializer;
import bali.String;

import static bali.Primitive.convert;

/**
 * User: Richard
 * Date: 01 Apr
 */
public class NumberSerializer implements Serializer<Number> {

	public Number parse(String in) {
		return NumberFactory.NUMBER_FACTORY.forDecimalString(convert(in).toCharArray());
	}

	public String format(Number in) {
		if (in instanceof bali.number.Byte) {
			return format((Byte) in);
		}
		if (in instanceof Short) {
			return format((Short) in);
		}
		if (in instanceof Int) {
			return format((Int) in);
		}
		if (in instanceof bali.number.Long) {
			return format((bali.number.Long) in);
		}
		throw new BaliThrowable("Cannot format Number " + in);
	}

	public String format(Byte in) {
		return new CharArrayString(java.lang.Byte.toString(in.value).toCharArray());
	}

	public String format(Short in) {
		return new CharArrayString(java.lang.Short.toString(in.value).toCharArray());
	}

	public String format(Int in) {
		return new CharArrayString(java.lang.Integer.toString(in.value).toCharArray());
	}

	public String format(bali.number.Long in) {
		return new CharArrayString(java.lang.Long.toString(in.value).toCharArray());
	}
}
