package bali.collection;

import bali.Boolean;
import bali.Integer;
import bali.Iterable;
import bali.Number;
import bali.annotation.Kind;
import bali.annotation.MetaType;

/**
 * User: Richard
 * Date: 16/08/13
 */
@MetaType(Kind.INTERFACE)
public interface Collection<T> extends Iterable<T> {

	public Integer size();

	public Boolean isEmpty();

	public T get(Number index);

}
