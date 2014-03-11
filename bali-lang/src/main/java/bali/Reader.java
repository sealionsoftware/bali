package bali;

import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.annotation.Nullable;
import bali.annotation.Operator;

/**
 * User: Richard
 * Date: 07/07/13
 */
@MetaType(Kind.INTERFACE)
public interface Reader {

	@Operator("<")
	@Nullable
	public Character read();

	@Operator("<<")
	@Nullable
	public String readLine();

}
