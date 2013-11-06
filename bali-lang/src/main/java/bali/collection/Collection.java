package bali.collection;

import bali.Iterable;
import bali.annotation.MetaType;
import bali.annotation.MetaTypes;
import bali.number.Integer;

/**
 * User: Richard
 * Date: 16/08/13
 */
@MetaType(MetaTypes.INTERFACE)
public interface Collection<T> extends Iterable<T> {

	public Integer size();

}
