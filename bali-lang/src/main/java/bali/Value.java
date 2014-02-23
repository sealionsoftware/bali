package bali;

import bali.annotation.Immutable;
import bali.annotation.MetaType;
import bali.annotation.Kind;
import bali.annotation.Name;
import bali.annotation.Operator;

/**
 * User: Richard
 * Date: 09/06/13
 */
@Immutable
@MetaType(Kind.INTERFACE)
public interface Value<T extends Value> {

	@Operator("==")
	public Boolean equalTo(@Name("operand") T operand);

}
