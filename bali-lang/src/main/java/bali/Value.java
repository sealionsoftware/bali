package bali;

import bali.annotation.Immutable;
import bali.annotation.MetaType;
import bali.annotation.MetaTypes;
import bali.annotation.Operator;

/**
 * User: Richard
 * Date: 09/06/13
 */
@Immutable
@MetaType(MetaTypes.INTERFACE)
public interface Value<T extends Value> {

	@Operator("==")
	public Boolean equalTo(T operand);

}
