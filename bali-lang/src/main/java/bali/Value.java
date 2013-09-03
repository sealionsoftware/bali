package bali;

import bali.annotation.Operator;

/**
 * User: Richard
 * Date: 09/06/13
 */
public interface Value<T extends Value> {

	@Operator("==")
	public Boolean equalTo(T operand);

}
