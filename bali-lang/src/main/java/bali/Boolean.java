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

	public static final Boolean TRUE = True.VALUE;
	public static final Boolean FALSE = False.VALUE;

	public static final java.lang.String AND = "&";
	public static final java.lang.String OR = "|";

	@Operator("!")
	Boolean not();

	@Operator(AND)
	Boolean and(Boolean operand);

	@Operator(OR)
	Boolean or(Boolean operand);

	@Operator("^")
	Boolean xor(Boolean that);

}
