package bali.type;

import bali.annotation.MetaType;
import bali.Type;

/**
 * User: Richard
 * Date: 27/08/13
 */
public interface Introspector {

	public @MetaType("O") <T extends Type, O> T getType(O o);
}
