package bali;

/**
 * User: Richard
 * Date: 15/07/13
 */
public interface Boolean extends Value<Boolean> {
	@Operator("!")
	Boolean not();

	@Operator("&")
	Boolean and(Boolean operand);

	@Operator("|")
	Boolean or(Boolean operand);

	@Operator("^")
	Boolean xor(Boolean that);

}
