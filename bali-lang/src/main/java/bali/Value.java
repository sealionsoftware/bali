package bali;

import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.annotation.Name;
import bali.annotation.Operator;

/**
 * User: Richard
 * Date: 09/06/13
 */
@MetaType(Kind.INTERFACE)
public interface Value<T extends Value> {

	@Operator("==")
	public Boolean equalTo(@Name("operand") T operand);

	@Operator("!=")
	public Boolean notEqualTo(@Name("operand") T operand);

}
