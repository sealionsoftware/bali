package bali;

import bali.annotation.Kind;
import bali.annotation.MetaType;

/**
 * User: Richard
 * Date: 28/02/14
 */
@MetaType(Kind.INTERFACE)
public interface Character extends Value<Character> {

	public Character toUpperCase();

	public Character toLowerCase();

}
