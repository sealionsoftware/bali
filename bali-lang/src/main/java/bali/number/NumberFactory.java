package bali.number;


import bali.CharArrayString;
import bali.Integer;
import bali.Number;
import bali.Serializer;
import bali.String;


/**
 * User: Richard
 * Date: 11/06/13
 */
public class NumberFactory implements Serializer<Number> {

	public static final NumberFactory NUMBER_FACTORY = new NumberFactory();

	private static final byte BYTE_CEILING = java.lang.Byte.MAX_VALUE;
	private static final byte BYTE_FLOOR = java.lang.Byte.MIN_VALUE;

	private static final Byte[] BYTES = new Byte[BYTE_CEILING - BYTE_FLOOR + 1];

	static {
		for (int b = BYTE_FLOOR; b <= BYTE_CEILING; b++) {
			BYTES[b - BYTE_FLOOR] = new Byte((byte) b);
		}
	}

	private static final short SHORT_CEILING = java.lang.Short.MAX_VALUE;
	private static final short SHORT_FLOOR = java.lang.Short.MIN_VALUE;

	private static final int INTEGER_CEILING = java.lang.Integer.MAX_VALUE;
	private static final int INTEGER_FLOOR = java.lang.Integer.MIN_VALUE;
	private static final char[] LONG_CEILING = java.lang.Long.toString(java.lang.Long.MAX_VALUE).toCharArray();
	private static final char[] LONG_FLOOR = java.lang.Long.toString(java.lang.Long.MIN_VALUE).substring(1).toCharArray();

	NumberFactory() {
	}

	public Number forDecimalString(char[] characters) {

		boolean negative = false;
		if (characters[0] == '-') {
			negative = true;
			char[] newCharacters = new char[characters.length - 1];
			System.arraycopy(characters, 1, newCharacters, 0, newCharacters.length);
			characters = newCharacters;
		}
		final char[] comparison = negative ? LONG_FLOOR : LONG_CEILING;

		if (characters.length > comparison.length) {
			return parseBigInteger(characters, negative);
		} else if (characters.length == comparison.length) {
			for (int i = 0; i < comparison.length; i++) {
				if (characters[i] > comparison[i]) {
					return parseBigInteger(characters, negative);
				} else if (characters[i] < comparison[i]) {
					break;
				}
			}
		}

		long value = 0;
		int i = 0;
		for (char character : characters) {
			if (!Character.isDigit(character)) {
				throw new RuntimeException("Invalid digit character " + character);
			}
			value += (java.lang.Integer.parseInt(Character.toString(character), 10) * Math.pow(10, characters.length - ++i));
		}
		if (negative) {
			value *= -1;
		}
		return forLong(value);
	}

	private BigInteger parseBigInteger(char[] characters, boolean negative) {

		int carry = 0;
		byte[] bytes = new byte[(int) Math.pow((double) characters.length, (double) 10 / 128)];
		int byteIndex = 0;
		for (int i = 0; i < characters.length; i++) {
			char character = characters[characters.length - i - 1];
			carry += Character.forDigit(character, 10);
			if (carry >= BYTE_CEILING) {
				bytes[byteIndex++] = (byte) (carry % BYTE_CEILING);
			}

		}
		BigInteger ret = new BigInteger(bytes);
		return negative ? ret.negative() : ret;
	}

	public Integer forLong(long o) {
		if (o <= INTEGER_CEILING && o >= INTEGER_FLOOR) {
			return forInt((int) o);
		}
		return new Long(o);
	}

	public Integer forInt(int o) {
		if (o <= SHORT_CEILING && o >= SHORT_FLOOR) {
			return forShort((short) o);
		}
		return new Int(o);
	}

	public Integer forShort(short o) {
		if (o <= BYTE_CEILING && o >= BYTE_FLOOR) {
			return forByte((byte) o);
		}
		return new Short(o);
	}

	public Integer forByte(byte o) {
		return BYTES[o - BYTE_FLOOR];
	}

	public int valueOf(Number n) {
		if (n instanceof Byte) {
			return ((Byte) n).value;
		}
		if (n instanceof Short) {
			return ((Short) n).value;
		}
		if (n instanceof Int) {
			return ((Int) n).value;
		}
		throw new RuntimeException("Cannot get int value of Number " + n);
	}

	public Number parse(String in) {
		throw new RuntimeException("Cannot parse Number " + in);
	}

	public String format(Number in) {
		if (in instanceof Byte) {
			return format((Byte) in);
		}
		if (in instanceof Short) {
			return format((Short) in);
		}
		if (in instanceof Int) {
			return format((Int) in);
		}
		if (in instanceof Long) {
			return format((Long) in);
		}
		throw new RuntimeException("Cannot format Number " + in);
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

	public String format(Long in) {
		return new CharArrayString(java.lang.Long.toString(in.value).toCharArray());
	}


}
