package bali.collection;

import bali.Value;
import bali.annotation.Kind;
import bali.annotation.MetaType;

/**
 * User: Richard
 * Date: 14/08/13
 */
@MetaType(Kind.INTERFACE)
public interface Set<T extends Value<T>> extends Collection<T> {

	public void add(T value);

	public void remove(T value);

	public Boolean contains(T value);

}
