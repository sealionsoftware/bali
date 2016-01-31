package bali.text;

import bali.Character;
import bali.Logic;

import static bali.Logic.FALSE;
import static bali.logic.Primitive.convert;

// TODO: extend this to work with full unicode
public final class CharCharacter implements Character {

	final char value;

	public static final Character[] CHARS = new Character[java.lang.Character.MAX_CODE_POINT];
	static {
		for (char c = 0 ; c < 256 ; c++){
			CHARS[c] = new CharCharacter(c);
		}
	}

	private CharCharacter(char value) {
		this.value = value;
	}

	public Character toUpperCase() {
		return new CharCharacter(java.lang.Character.toUpperCase(value));
	}

	public Character toLowerCase() {
		return new CharCharacter(java.lang.Character.toLowerCase(value));
	}

	public Logic equalTo(Character operand) {
		if (operand instanceof CharCharacter){
			return convert(operand == this);
		}
		return FALSE;
	}

	public Logic notEqualTo(Character operand) {
		return equalTo(operand).not();
	}

	@Override
	public String toString() {
		return java.lang.Character.toString(value);
	}

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CharCharacter that = (CharCharacter) o;
        return value == that.value;
    }

    public int hashCode() {
        return (int) value;
    }
}
