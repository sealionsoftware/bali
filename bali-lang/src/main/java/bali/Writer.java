package bali;

import bali.annotation.MetaType;
import bali.annotation.MetaTypes;
import bali.annotation.Monitor;
import bali.annotation.Operator;

/**
 * User: Richard
 * Date: 07/07/13
 */
@MetaType(MetaTypes.INTERFACE)
public interface Writer {

	@Operator("<<")
	public void writeLine(String in);

}
