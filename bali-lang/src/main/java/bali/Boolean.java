package bali;

import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.annotation.Operator;

/**
 * User: Richard
 * Date: 15/07/13
 */
@MetaType(Kind.INTERFACE)
public interface Boolean extends Value<Boolean> {

	@Operator("~")
	Boolean not();

	@Operator("&")
	Boolean and(Boolean operand);

	@Operator("|")
	Boolean or(Boolean operand);

	@Operator("^")
	Boolean xor(Boolean that);

}
