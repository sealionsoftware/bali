package bali;

import bali.annotation.MetaType;
import bali.annotation.MetaTypes;

/**
 * User: Richard
 * Date: 09/06/13
 */
@MetaType(MetaTypes.INTERFACE)
public interface Iterator<T> {

	public bali.Boolean hasNext();

	public T next();

}
