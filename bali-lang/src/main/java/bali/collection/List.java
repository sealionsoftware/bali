package bali.collection;

import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.Integer;

/**
 * User: Richard
 * Date: 14/08/13
 */
@MetaType(Kind.INTERFACE)
public interface List<T> extends Collection<T> {

	public T get(Integer index);

	public void set(Integer index, T object);

	public void add(T object);

	public void remove(Integer index);

}
