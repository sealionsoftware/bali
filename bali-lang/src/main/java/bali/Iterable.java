package bali;

import bali.annotation.Kind;
import bali.annotation.MetaType;

/**
 * User: Richard
 * Date: 09/06/13
 */
@MetaType(Kind.INTERFACE)
public interface Iterable<T> {

	public Iterator<T> iterator();

}
