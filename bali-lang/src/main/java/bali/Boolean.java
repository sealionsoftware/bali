package bali;

import bali.annotation.Operator;
import bali.logic.False;
import bali.logic.True;

public interface Boolean extends Value<Boolean> {

	Boolean TRUE = True.VALUE;
	Boolean FALSE = False.VALUE;

    @Operator("!")
	Boolean not();

    @Operator("&")
	Boolean and(Boolean operand);

    @Operator("|")
	Boolean or(Boolean operand);

    @Operator("^")
	Boolean xor(Boolean that);

}
