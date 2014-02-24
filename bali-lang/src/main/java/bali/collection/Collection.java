package bali.collection;

import bali.Integer;
import bali.Iterable;
import bali.annotation.Kind;
import bali.annotation.MetaType;

/**
 * User: Richard
 * Date: 16/08/13
 */
@MetaType(Kind.INTERFACE)
public interface Collection<T> extends Iterable<T> {

	public Integer size();

}
