package bali;

import bali.collection.Array;
import bali.number.NumberFactory;

import java.util.Arrays;

/**
 * User: Richard
 * Date: 02/05/13
 */
public final class CharArrayString implements String {

	final char[] characters;

	public CharArrayString(char[] characters) {
		this.characters = characters;
	}

	public Iterable<String> getCharacters() {
		String[] characterStrings = new String[characters.length];
		int i = 0;
		for (char character : characters){
			characterStrings[i++] = new CharArrayString(new char[]{character});
		}
		return new Array<>(characterStrings);
	}

	public Number length(){
		return NumberFactory.NUMBER_FACTORY.forInt(characters.length);
	}

	public String join(String operand){
		CharArrayString cas = (CharArrayString) operand;
		int thisLength = this.characters.length;
		int thatLength = cas.characters.length;
		char[] characters = Arrays.copyOf(this.characters, thisLength + thatLength);
		System.arraycopy(cas.characters, 0, characters, thisLength, thatLength);
		return new CharArrayString(characters);
	}

	public Boolean equalTo(String operand) {
		CharArrayString cas = (CharArrayString) operand;
		return Arrays.equals(characters, cas.characters) ? IdentityBoolean.TRUE : IdentityBoolean.FALSE;
	}

	public String uppercase() {
		char[] upper = new char[characters.length];
		int i = 0;
		for(char character : characters){
			upper[i++] = Character.toUpperCase(character);
		}
		return new CharArrayString(upper);
	}

	public java.lang.String toString() {
		return new java.lang.String(characters);
	}
}
