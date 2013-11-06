package bali.collection;

import bali.annotation.MetaType;
import bali.annotation.MetaTypes;
import bali.number.Integer;

/**
 * User: Richard
 * Date: 14/08/13
 */
@MetaType(MetaTypes.INTERFACE)
public interface List<T> extends Collection<T> {

	public T get(Integer index);

	public void set(Integer index, T object);

	public void add(T object);

	public void remove(Integer index);

}
