package bali.collection.standard;

import bali.Boolean;
import bali.Integer;
import bali.Iterator;
import bali.Number;
import bali.Primitive;
import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.collection.Collection;
import bali.collection.List;

import static bali.Primitive.convert;

/**
 * TODO
 * User: Richard
 * Date: 27/08/13
 */
@MetaType(Kind.OBJECT)
public final class LinkedList<T> implements List<T> {

	public LinkedList(){}
	private int size;

	public LinkedList(Collection<T> in) {
		Iterator<T> iterator = in.iterator();
		while (convert(iterator.hasNext())){
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
		return Primitive.convert(size);
	}

	public void add(T object) {
	}

	public void remove(Integer index) {
	}

	public Iterator<T> iterator() {

		return new Iterator<T>() {

			private Link<T> current = start;

			public bali.Boolean hasNext() {
				return convert(start.next != null);
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

	public Boolean isEmpty() {
		return convert(size == 0);
	}

	public Collection<T> join(Collection<T> operand) {
		return null;
	}

	public Collection<T> head(Integer index) {
		return null;
	}

	public Collection<T> tail(Integer index) {
		return null;
	}
}
