package bali;

import bali.number.NumberFactory;

/**
 * User: Richard
 * Date: 26/02/14
 */
public class Primitive {

	public static Integer convert(int in) {
		return NumberFactory.NUMBER_FACTORY.forInt(in);
	}

	public static int convert(Integer in) {
		return NumberFactory.NUMBER_FACTORY.valueOf(in);
	}

	public static Integer convert(long in) {
		return NumberFactory.NUMBER_FACTORY.forLong(in);
	}

//	public static long convert(Long in) {
//		return NumberFactory.NUMBER_FACTORY.valueOf(in);
//	}

	public static Boolean convert(boolean in) {
		return in ? True.TRUE : False.FALSE;
	}

	public static boolean convert(Boolean in) {
		return in == True.TRUE;
	}

	public static String convert(char[] in) {
		return new CharArrayString(in);
	}

	public static String convert(java.lang.String in) {
		return convert(in.toCharArray());
	}
}
