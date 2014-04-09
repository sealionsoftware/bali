package bali.collection;

import bali.Boolean;
import bali.Integer;
import bali.Iterator;
import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.annotation.Nullable;
import bali.annotation.Parameters;
import bali.type.Type;

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

	public LinkedList() {
	}

	@Parameters
	public LinkedList(Type T, @Nullable Collection<T> in) {
		if (in != null){
			Iterator<T> iterator = in.iterator();
			while (convert(iterator.hasNext())){
				add(iterator.next());
			}
		}
	}

	public T get(Integer index) {
		Link<T> l = getLink(convert(index));
		return l != null ? l.item : null;
	}

	public void set(Integer index, T object) {
		Link<T> l = getLink(convert(index));
		if (l != null){
			getLink(convert(index)).item = object;
		}
	}

	private Link<T> getLink(long i){
		if (i < 1 || i > size){
			return null;
		}
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
			if (before != null){
				before.next = before.next.next;
			}
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

	private static class Link<T> {
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
		int i = convert(index);
		if (i < size){
			if (i < 1){
				clear();
			} else {
				last = getLink(i);
				size = i;
			}
		}
		return this;
	}

	public Collection<T> tail(Integer index) {
		int i = convert(index);
		if (i > 1){
			if (i > size){
				clear();
			} else {
				first = getLink(i);
				size = size - i + 1;
			}
		}
		return this;
	}

	private void clear() {
		last = first = new Link<>();
		size = 0;
	}
}
