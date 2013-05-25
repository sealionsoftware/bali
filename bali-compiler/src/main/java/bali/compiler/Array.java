package bali.compiler;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * User: Richard
 * Date: 08/05/13
 */
public class Array<E> implements List<E> {

	private E[] elements;

	public Array(E[] elements){
		this.elements = elements;
	}

	public int size() {
		return elements.length;
	}

	public boolean isEmpty() {
		return elements.length == 0;
	}

	public boolean contains(Object o) {
		for (int i = 0 ; i < elements.length ; i++){
			if (equal(o, elements[i])){
				return true;
			}
		}
		return false;
	}

	private boolean equal(Object o, E element){
		return o == null ? element == null : element != null && element.equals(o);
	}

	public Iterator<E> iterator() {
		return listIterator();
	}

	public Object[] toArray() {
		return elements;
	}

	public <T> T[] toArray(T[] a) {
		return Arrays.copyOf(elements, elements.length, (Class<T[]>) a.getClass().getComponentType());
	}

	public boolean add(E e) {
		add(elements.length, e);
		return true;
	}

	public boolean remove(Object o) {
		int i = indexOf(o);
		if (i > -1){
			remove(i);
			return true;
		}
		return false;
	}

	public boolean containsAll(Collection<?> c) {
		for (Object o : c){
			if (!contains(o)){
				return false;
			}
		}
		return true;
	}

	public boolean addAll(Collection<? extends E> c) {
		boolean ret = false;
		for (E element : c){
			ret |= add(element);
		}
		return ret;
	}

	public boolean addAll(int index, Collection<? extends E> c) {
		E[] initial = elements;
		for (E element : c){
			add(index++, element);
		}
		return Arrays.equals(elements, initial);
	}

	public boolean removeAll(Collection<?> c) {
		for (Object o : c){
			remove(o);
		}
		return false;
	}

	public boolean retainAll(Collection<?> c) {
		boolean ret = false;
		for (E element : elements){
			if (!c.contains(element)){
				remove(element);
				ret = true;
			}
		}
		return ret;
	}

	public void clear() {
		elements = (E[]) new Object[0];
	}

	public E get(int index) {
		return elements[index];
	}

	public E set(int index, E element) {
		E prev = elements[index];
		elements[index] = element;
		return prev;
	}

	public void add(int index, E element) {
		E[] newElements = (E[]) new Object[elements.length + 1];
		System.arraycopy(elements, 0, newElements, 0, index - 1);
		newElements[index] = element;
		System.arraycopy(elements, index + 1, newElements, index, elements.length - (index + 1));
	}

	public E remove(int index) {
		E[] newElements = (E[]) new Object[elements.length - 1];
		System.arraycopy(elements, 0, newElements, 0, index - 1);
		System.arraycopy(elements, index, newElements, index - 1, elements.length - (index + 1));
		return elements[index];
	}

	public int indexOf(Object o) {
		for (int i = 0 ; i < elements.length ; i++){
			if (equal(o, elements[i])){
				return i;
			}
		}
		return -1;
	}

	public int lastIndexOf(Object o) {
		for (int i = elements.length - 1 ; i >= 0 ; i--){
			if (equal(o, elements[i])){
				return i;
			}
		}
		return -1;
	}

	public ListIterator<E> listIterator() {
		return listIterator(0);
	}

	public ListIterator<E> listIterator(int index) {
		return new ListIterator<E>() {

			int i = 0;

			public boolean hasNext() {
				return i < elements.length;
			}

			public E next() {
				return elements[i++];
			}

			public boolean hasPrevious() {
				return i > 0;
			}

			public E previous() {
				return elements[--i];
			}

			public int nextIndex() {
				return i + 1;
			}

			public int previousIndex() {
				return i - 1;
			}

			public void remove() {
				Array.this.remove(i);
			}

			public void set(E e) {
				Array.this.set(i, e);
			}

			public void add(E e) {
				Array.this.add(e);
			}
		};
	}

	public List<E> subList(int fromIndex, int toIndex) {
		E[] ret = (E[]) new Object[toIndex - fromIndex];
		System.arraycopy(elements, fromIndex, ret, 0, ret.length);
		return new Array<E>(ret);
	}
}
