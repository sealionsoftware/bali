package bali;

import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.annotation.Name;

/**
 * User: Richard
 * Date: 04/07/13
 */
@MetaType(Kind.INTERFACE)
public interface Parser<T> {

	public T parse(@Name("serialization") String serialization);

}
