package bali;

import bali.logic.False;
import bali.logic.True;

public interface Boolean extends Value<Boolean> {

	Boolean TRUE = True.VALUE;
	Boolean FALSE = False.VALUE;

	Boolean not();

	Boolean and(Boolean operand);

	Boolean or(Boolean operand);

	Boolean xor(Boolean that);

}
