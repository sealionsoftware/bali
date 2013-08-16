package bali.collection;

import bali.number.Integer;

/**
 * User: Richard
 * Date: 14/08/13
 */
public interface List<T> extends Collection<T> {

	public T get(Integer index);

	public void set(Integer index);

}
