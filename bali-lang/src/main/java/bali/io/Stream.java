package bali.io;

import bali.Value;
import bali.Iterator;
import bali.annotation.Kind;
import bali.annotation.MetaType;

/**
 * User: Richard
 * Date: 06/02/14
 */
@MetaType(Kind.INTERFACE)
public interface Stream<T extends Value> extends Iterator<T> {

	public void write(T next);

}
