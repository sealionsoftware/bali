package bali.collection;

import bali.Value;
import bali.annotation.MetaType;
import bali.annotation.MetaTypes;

/**
 * User: Richard
 * Date: 14/08/13
 */
@MetaType(MetaTypes.INTERFACE)
public interface Set<T extends Value<T>> extends Collection<T> {

	public void add(T value);

	public void remove(T value);

	public Boolean contains(T value);

}
