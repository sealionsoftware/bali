package bali;

import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.annotation.Name;
import bali.annotation.Operator;

/**
 * User: Richard
 * Date: 07/07/13
 */
@MetaType(Kind.INTERFACE)
public interface Writer {

	@Operator("<")
	public void write(@Name("in") Character in);

	@Operator("<<")
	public void writeLine(@Name("in") String in);

}
