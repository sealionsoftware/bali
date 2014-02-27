package bali.collection.standard;

import bali.Integer;
import bali.Iterator;
import bali._;
import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.collection.Collection;
import bali.collection.List;

/**
 * User: Richard
 * Date: 27/08/13
 */
@MetaType(Kind.OBJECT)
public final class LinkedList<T> implements List<T> {

	public LinkedList(){}
	private int size;

	public LinkedList(Collection<T> in) {
		Iterator<T> iterator = in.iterator();
		while (_.PRIMITIVE_CONVERTER.from(iterator.hasNext())){
			add(iterator.next());
		}
	}

	private Link<T> start;

	public T get(Integer index) {
		return null;
	}

	public void set(Integer index, T object) {

	}

	public Integer size() {
		return _.PRIMITIVE_CONVERTER.from(size);
	}

	public void add(T object) {
	}

	public void remove(Integer index) {
	}

	public Iterator<T> iterator() {

		return new Iterator<T>() {

			private Link<T> current = start;

			public bali.Boolean hasNext() {
				return _.PRIMITIVE_CONVERTER.from(start.next != null);
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
