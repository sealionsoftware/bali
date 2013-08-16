package bali.collection;

import bali.number.Integer;
import bali.Iterable;

/**
 * User: Richard
 * Date: 16/08/13
 */
public interface Collection<T> extends Iterable<T> {

	public Integer size();

}
