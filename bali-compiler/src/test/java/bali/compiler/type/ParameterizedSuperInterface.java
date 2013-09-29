package bali.compiler.type;

import bali.String;
import bali.Value;
import bali.annotation.Operator;

/**
 * User: Richard
 * Date: 28/09/13
 */
public interface ParameterizedSuperInterface<T extends C> {

	public T aMethod();

	@Operator("#")
	public T anUnaryOperator();

	@Operator("~")
	public void anOperator(T operand);

}
