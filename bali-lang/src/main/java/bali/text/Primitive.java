package bali.text;

import bali.Character;
import bali.Text;

public final class Primitive {

	public static Text convert(String in) {
		return new CharArrayString(in.toCharArray());
	}

	public static String convert(Text in) {
		if (in instanceof CharArrayString){
			return new String(((CharArrayString) in).characters);
		}
		throw new RuntimeException("Cannot convert String of type " + in.getClass());
	}

	public static Character convert(char in) {
		return CharCharacter.CHARS[in];
	}

	public static char convert(Character in) {
		return ((CharCharacter) in).value;
	}

}
