package bali;

import bali.annotation.MetaType;
import bali.annotation.MetaTypes;
import bali.annotation.Name;

/**
 * User: Richard
 * Date: 04/07/13
 */
@MetaType(MetaTypes.INTERFACE)
public interface Formatter<T extends Value> {

	public String format(@Name("in") T in);

}
