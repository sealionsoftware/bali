package bali.io;

import bali.Value;
import bali.Iterator;
import bali.annotation.MetaType;
import bali.annotation.MetaTypes;

/**
 * User: Richard
 * Date: 06/02/14
 */
@MetaType(MetaTypes.INTERFACE)
public interface Stream<T extends Value> extends Iterator<T> {

	public void write(T next);

}
