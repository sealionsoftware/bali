package bali;

import bali.annotation.Kind;
import bali.annotation.MetaType;

/**
 * User: Richard
 * Date: 09/06/13
 */
@MetaType(Kind.INTERFACE)
public interface Iterator<T> {

	public bali.Boolean hasNext();

	public T next();

}
