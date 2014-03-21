package bali;

import bali.collection.Collection;
import bali.number.NumberFactory;

import java.util.ArrayList;

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

	public static Boolean convert(boolean in) {
		return in ? Boolean.TRUE : Boolean.FALSE;
	}

	public static boolean convert(Boolean in) {
		return in == Boolean.TRUE;
	}

	public static String convert(char[] in) {
		return new CharArrayString(in);
	}

	public static String convert(java.lang.String in) {
		return new CharArrayString(in.toCharArray());
	}

	public static java.lang.String convert(String in) {
		if (in instanceof CharArrayString){
			return new java.lang.String(((CharArrayString) in).characters);
		}
		if (in instanceof StringBuilder){
			return new java.lang.String(((StringBuilder) in).getChars());
		}
		throw new java.lang.RuntimeException("Cannot convert String of type " + in.getClass());
	}

	public static Character convert(char in) {
		return CharCharacter.CHARS[in];
	}

	public static char convert(Character in) {
		return ((CharCharacter) in).value;
	}

	public static <T> ArrayList<T> convert(Collection<T> in) {
		ArrayList<T> ret = new ArrayList<>();
		Iterator<T> i = in.iterator();
		while(convert(i.hasNext())){
			ret.add(i.next());
		}
		return ret;
	}
}
