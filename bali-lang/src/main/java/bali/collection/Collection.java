package bali.collection;

import bali.Iterable;
import bali.annotation.MetaType;
import bali.annotation.Kind;
import bali.Integer;

/**
 * User: Richard
 * Date: 16/08/13
 */
@MetaType(Kind.INTERFACE)
public interface Collection<T> extends Iterable<T> {

	public Integer size();

}
