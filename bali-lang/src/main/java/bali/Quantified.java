package bali;

import bali.annotation.MetaType;
import bali.annotation.MetaTypes;
import bali.annotation.Operator;

/**
 * User: Richard
 * Date: 05/07/13
 */
@MetaType(MetaTypes.INTERFACE)
public interface Quantified<T extends Value> extends Value<T> {

	@Operator(">")
	public Boolean greaterThan(T o);

	@Operator("<")
	public Boolean lessThan(T o);


}
