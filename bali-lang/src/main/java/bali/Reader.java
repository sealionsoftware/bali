package bali;

import bali.annotation.MetaType;
import bali.annotation.MetaTypes;
import bali.annotation.Operator;

/**
 * User: Richard
 * Date: 07/07/13
 */
@MetaType(MetaTypes.INTERFACE)
public interface Reader {

	@Operator("<<")
	public String readLine();

}
