package bali;

import java.util.Arrays;

import static bali._.NUMBER_FACTORY;

/**
 * User: Richard
 * Date: 02/05/13
 */
public class String implements Value<String> {

	final char[] characters;

	String(char[] characters) {
		this.characters = characters;
	}

	public Array<String> getCharacters() {
		String[] characterStrings = new String[characters.length];
		int i = 0;
		for (char character : characters){
			characterStrings[i++] = new String(new char[]{character});
		}
		return new Array<>(characterStrings);
	}

	public Number length(){
		return NUMBER_FACTORY.forInt(characters.length);
	}

	public Boolean equalTo(String operand) {
		return Arrays.equals(characters, operand.characters) ? Boolean.TRUE : Boolean.FALSE;
	}

//	public java.lang.String toString() {
//		return new java.lang.String(characters);
//	}
}
