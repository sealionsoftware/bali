package bali.text;

import bali.*;
import bali.Character;
import bali.Integer;

import java.util.Arrays;

import static bali.Logic.FALSE;
import static bali.Logic.TRUE;
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

    public Text concatenate(Text operand) {
        int originalLength = characters.length;
        if (operand instanceof CharArrayString){
            char[] otherCharacters =  ((CharArrayString) operand).characters;
            char[] newChars = Arrays.copyOf(characters, originalLength + otherCharacters.length);
            System.arraycopy(otherCharacters, 0 , newChars, originalLength, otherCharacters.length);
            return new CharArrayString(newChars);
        }

        int newLength = originalLength + convert(operand.size());
        char[] newChars = Arrays.copyOf(characters, newLength);
        Iterator<Character> characterIterator = operand.iterator();
        for (int i = characters.length ; i < newLength ; i++){
            newChars[i] = convert(characterIterator.next());
        }

        return new CharArrayString(newChars);
    }

    public Logic contains(Character value) {
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

	public Logic isEmpty() {
		return convert(characters.length == 0);
	}

	public Character get(Integer index) {
        int i = convert(index);
        return i > 0 && i <= characters.length ? convert(characters[i - 1]) : null;
	}

    @Override
    public Group<Character> head(Integer number) {
        throw new NotImplementedException();
    }

    @Override
    public Group<Character> tail(Integer number) {
        throw new NotImplementedException();
    }

    public Iterator<Character> iterator() {
		return new Iterator<Character>() {

			int i = 0;

			public Logic hasNext() {
				return convert(i < characters.length);
			}

			public Character next() {
				return i < characters.length ? CharCharacter.CHARS[characters[i++]] : null;
			}
		};
	}

	public Logic equalTo(Text operand) {

		if (operand instanceof CharArrayString){
			CharArrayString cas = (CharArrayString) operand;
			return convert(Arrays.equals(characters, cas.characters));
		}

        if (characters.length != convert(operand.size())){
            return Logic.FALSE;
        }

        Iterator<Character> otherCharacters = operand.iterator();
        for (char character : characters) if (convert(CharCharacter.CHARS[character].notEqualTo(otherCharacters.next()))) {
            return Logic.FALSE;
        }

        return Logic.TRUE;
	}

	public Logic notEqualTo(Text operand) {

		if (operand instanceof CharArrayString){
			CharArrayString cas = (CharArrayString) operand;
			return convert(!Arrays.equals(characters, cas.characters));
		}

        if (characters.length != convert(operand.size())){
            return Logic.TRUE;
        }

        Iterator<Character> otherCharacters = operand.iterator();
        for (char character : characters) if (convert(CharCharacter.CHARS[character].notEqualTo(otherCharacters.next()))) {
            return Logic.TRUE;
        }

        return Logic.FALSE;
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
