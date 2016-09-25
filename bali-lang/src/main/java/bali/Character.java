package bali;

import bali.text.CharCharacter;

/**
 * User: Richard
 * Date: 28/02/14
 */
public interface Character extends Value<Character> {

    Character NEW_LINE = CharCharacter.CHARS['\n'];

	Character toUpperCase();

	Character toLowerCase();

}
