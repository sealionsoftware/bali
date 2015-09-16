package bali;

/**
 * User: Richard
 * Date: 09/06/13
 */
public interface Value<T extends Value> {

	Boolean equalTo(T operand);

	Boolean notEqualTo(T operand);

}
