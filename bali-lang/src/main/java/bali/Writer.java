package bali;

import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.annotation.Operator;

/**
 * User: Richard
 * Date: 07/07/13
 */
@MetaType(Kind.INTERFACE)
public interface Writer {

	@Operator("<")
	public void write(Character in);

	@Operator("<<")
	public void writeLine(String in);

}
