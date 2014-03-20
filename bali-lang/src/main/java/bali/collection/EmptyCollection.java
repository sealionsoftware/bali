package bali.collection;

import bali.Boolean;
import bali.Integer;
import bali.Iterator;
import bali.type.Type;

/**
 * User: Richard
 * Date: 19/03/14
 */
public class EmptyCollection<T> implements Collection<T> {

	public EmptyCollection(Type T) {
	}

	public bali.Integer size() {
		return bali.number._.ZERO;
	}

	public Boolean isEmpty() {
		return Boolean.TRUE;
	}

	public T get(Integer index) {
		return null;
	}

	public Collection<T> join(Collection<T> operand) {
		return operand;
	}

	public Collection<T> head(Integer index) {
		return this;
	}

	public Collection<T> tail(Integer index) {
		return this;
	}

	public Iterator<T> iterator() {
		return new Iterator<T>() {
			public Boolean hasNext() {
				return Boolean.FALSE;
			}

			public T next() {
				return null;
			}
		};
	}
}
