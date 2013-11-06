package bali;

import bali.annotation.MetaType;
import bali.annotation.MetaTypes;

/**
 * User: Richard
 * Date: 09/06/13
 */
@MetaType(MetaTypes.INTERFACE)
public interface Iterable<T> {

	public Iterator<T> iterator();

}
