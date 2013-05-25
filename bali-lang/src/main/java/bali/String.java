package bali;

import java.util.Arrays;

/**
 * User: Richard
 * Date: 02/05/13
 */
public class String {

	final char[] characters;

	public String(char[] characters) {
		this.characters = characters;
	}

	public Number length(){
		return new Number(characters.length);
	}

	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		return Arrays.equals(characters, ((String) o).characters);
	}

	public java.lang.String toString() {
		return new java.lang.String(characters);
	}
}
