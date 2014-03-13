package bali.collection;

import bali.Boolean;
import bali.Integer;
import bali.Iterator;
import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.annotation.Nullable;

import static bali.Primitive.convert;

/**
 * User: Richard
 * Date: 27/08/13
 */
@MetaType(Kind.OBJECT)
public final class LinkedList<T> implements List<T> {

	private int size = 0;

	private Link<T> first = new Link<>();
	private Link<T> last = first;

	public LinkedList(@Nullable Collection<T> in) {
		if (in != null){
			Iterator<T> iterator = in.iterator();
			while (convert(iterator.hasNext())){
				add(iterator.next());
			}
		}
	}

	public T get(Integer index) {
		return getLink(convert(index)).item;
	}

	public void set(Integer index, T object) {
		getLink(convert(index)).item = object;
	}

	private Link<T> getLink(int i){
		Link<T> current = first;
		while (--i > 0){
			current = current.next;
		}
		return current;
	}

	public Integer size() {
		return convert(size);
	}

	public void add(T object) {
		Link<T> newLink = new Link<>();
		last.item = object;
		last.next = newLink;
		last = newLink;
		size++;
	}

	public void remove(Integer index) {
		int i = convert(index);
		if (i == 1){
			first = first.next;
		} else if (i == size){
			getLink(i - 1).next = null;
		} else {
			Link before = getLink(i - 1);
			before.next = before.next.next;
		}
		size--;
	}

	public Iterator<T> iterator() {

		return new Iterator<T>() {

			private Link<T> current = first;

			public bali.Boolean hasNext() {
				return convert(current.next != null);
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
		Iterator<T> iterator = operand.iterator();
		while (convert(iterator.hasNext())){
			add(iterator.next());
		}
		return this;
	}

	public Collection<T> head(Integer index) {
		first = getLink(convert(index));
		return this;
	}

	public Collection<T> tail(Integer index) {
		last = getLink(convert(index));
		return this;
	}
}
