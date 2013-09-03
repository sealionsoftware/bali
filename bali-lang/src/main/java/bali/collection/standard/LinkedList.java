package bali.collection.standard;

import bali.IdentityBoolean;
import bali.Iterator;
import bali.collection.Collection;
import bali.collection.List;
import bali.number.Integer;

/**
 * User: Richard
 * Date: 27/08/13
 */
public final class LinkedList<T> implements List<T> {

	public LinkedList(){}

	public LinkedList(Collection<T> in) {
		Iterator<T> iterator = in.iterator();
		while (iterator.hasNext() == IdentityBoolean.TRUE){

		}
	}

	private Link<T> start;

	public T get(Integer index) {
		return null;
	}

	public void set(Integer index, T object) {

	}

	public Integer size() {
		return null;
	}

	public void add(T object) {
	}

	public void remove(Integer index) {
	}

	public Iterator<T> iterator() {

		return new Iterator<T>() {

			private Link<T> current = start;

			public bali.Boolean hasNext() {
				return start.next != null ? IdentityBoolean.TRUE : IdentityBoolean.FALSE;
			}

			public T next() {
				T ret = current.item;
				current = current.next;
				return ret;
			}
		};
	}

	private class Link<T> {
		public Link<T> next;
		public T item;
	}
}
