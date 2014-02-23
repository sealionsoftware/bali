package bali;

import bali.annotation.MetaType;
import bali.annotation.Kind;
import bali.annotation.Name;

/**
 * User: Richard
 * Date: 04/07/13
 */
@MetaType(Kind.INTERFACE)
public interface Formatter<T extends Value> {

	public String format(@Name("in") T in);

}
