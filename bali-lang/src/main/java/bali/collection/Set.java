package bali.collection;

import bali.Value;

/**
 * User: Richard
 * Date: 14/08/13
 */
public interface Set<T extends Value<T>> extends Collection<T> {

	public void add(T value);

	public void remove(T value);

	public Boolean contains(T value);

}
