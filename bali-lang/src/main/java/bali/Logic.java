package bali;

import bali.annotation.Operator;
import bali.logic.False;
import bali.logic.True;

public interface Logic extends Value<Logic> {

	Logic TRUE = True.VALUE;
	Logic FALSE = False.VALUE;

    @Operator("!")
    Logic not();

    @Operator("&")
    Logic and(Logic operand);

    @Operator("|")
    Logic or(Logic operand);

    @Operator("^")
    Logic xor(Logic that);

}
