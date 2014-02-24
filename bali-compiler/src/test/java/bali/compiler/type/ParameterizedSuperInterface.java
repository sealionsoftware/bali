package bali.compiler.type;

import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.annotation.Operator;

/**
 * User: Richard
 * Date: 28/09/13
 */
@MetaType(Kind.INTERFACE)
public interface ParameterizedSuperInterface<T extends C> {

	public T aMethod();

	@Operator("#")
	public T anUnaryOperator();

	@Operator("~")
	public void anOperator(T operand);

}
