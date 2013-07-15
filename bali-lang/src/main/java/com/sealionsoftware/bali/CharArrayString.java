package com.sealionsoftware.bali;

import bali.*;
import bali.Boolean;
import bali.Number;
import bali.String;
import com.sealionsoftware.bali.collections.Array;

import java.lang.Iterable;
import java.util.Arrays;

import static bali._.NUMBER_FACTORY;

/**
 * User: Richard
 * Date: 02/05/13
 */
public class CharArrayString implements String {

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
		return NUMBER_FACTORY.forInt(characters.length);
	}

	@Operator("+")
	public String join(String operand){
		int thisLength = this.characters.length;
		int thatLength = operand.characters.length;
		char[] characters = Arrays.copyOf(this.characters, thisLength + thatLength);
		System.arraycopy(operand.characters, 0, characters, thisLength, thatLength);
		return new CharArrayString(characters);
	}

	public Boolean equalTo(CharArrayString operand) {
		return Arrays.equals(characters, operand.characters) ? IdentityBoolean.TRUE : IdentityBoolean.FALSE;
	}

	public java.lang.String toString() {
		return new java.lang.String(characters);
	}
}
