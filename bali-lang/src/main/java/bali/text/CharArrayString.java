package bali.text;

import bali.Boolean;
import bali.Character;
import bali.Integer;
import bali.Iterator;
import bali.Text;

import java.util.Arrays;

import static bali.Boolean.FALSE;
import static bali.Boolean.TRUE;
import static bali.logic.Primitive.convert;
import static bali.number.Primitive.convert;
import static bali.text.Primitive.convert;

public final class CharArrayString implements Text {

	public final char[] characters;
	private static final Text EMPTY_STRING = new CharArrayString(new char[0]);

	CharArrayString(char[] characters) {
		this.characters = characters;
	}

	public Text uppercase() {
		char[] upper = new char[characters.length];
		int i = 0;
		for(char character : characters){
			upper[i++] = java.lang.Character.toUpperCase(character);
		}
		return new CharArrayString(upper);
	}

	public Boolean contains(Character value) {
		for (char c : characters){
			if (value == CharCharacter.CHARS[c]){
				return TRUE;
			}
		}
		return FALSE;
	}

	public Integer size() {
		return convert(characters.length);
	}

	public Boolean isEmpty() {
		return convert(characters.length == 0);
	}

	public Character get(Integer index) {
		return convert(characters[convert(index)]);
	}

	public Iterator<Character> iterator() {
		return new Iterator<Character>() {

			int i = 0;

			public Boolean hasNext() {
				return convert(i < characters.length);
			}

			public Character next() {
				return i < characters.length ? CharCharacter.CHARS[characters[i++]] : null;
			}
		};
	}

	public Boolean equalTo(Text operand) {

		if (operand instanceof CharArrayString){
			CharArrayString cas = (CharArrayString) operand;
			return convert(Arrays.equals(characters, cas.characters));
		}

		return FALSE;
	}

	public Boolean notEqualTo(Text operand) {

		if (operand instanceof CharArrayString){
			CharArrayString cas = (CharArrayString) operand;
			return convert(!Arrays.equals(characters, cas.characters));
		}
		return TRUE;
	}

	public java.lang.String toString() {
		return new java.lang.String(characters);
	}

    public boolean equals(Object o) {
        return this == o
                || !(o == null
                || getClass() != o.getClass())
                    && Arrays.equals(characters, ((CharArrayString) o).characters);

    }

    public int hashCode() {
        return Arrays.hashCode(characters);
    }
}
