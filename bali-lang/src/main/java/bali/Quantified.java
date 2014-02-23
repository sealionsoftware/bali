package bali;

import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.annotation.Operator;

/**
 * User: Richard
 * Date: 05/07/13
 */
@MetaType(Kind.INTERFACE)
public interface Quantified<T extends Value> extends Value<T> {

	@Operator(">")
	public Boolean greaterThan(T o);

	@Operator("<")
	public Boolean lessThan(T o);


}
