package bali.text;

import bali.Boolean;
import bali.Character;

import static bali.Boolean.FALSE;
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

	public Boolean equalTo(Character operand) {
		if (operand instanceof CharCharacter){
			return convert(operand == this);
		}
		return FALSE;
	}

	public Boolean notEqualTo(Character operand) {
		return equalTo(operand).not();
	}

	@Override
	public String toString() {
		return java.lang.Character.toString(value);
	}
}
