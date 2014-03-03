package bali;

import bali.annotation.Name;

import static bali.Boolean.FALSE;
import static bali.Primitive.convert;

/**
 * TODO: extend this to work with full unicode
 *
 * User: Richard
 * Date: 01/03/14
 */
public class CharCharacter implements Character {

	char value;

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

	public Boolean equalTo(@Name("operand") Character operand) {
		if (operand instanceof CharCharacter){
			return convert(operand == this);
		}
		return FALSE;
	}

	public Boolean notEqualTo(@Name("operand") Character operand) {
		return equalTo(operand).not();
	}
}
