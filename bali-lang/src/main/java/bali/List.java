package bali;

import java.lang.*;
import java.lang.String;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;

/**
 * User: Richard
 * Date: 08/05/13
 */
public class List<E>  {

	private E[] elements;

	public List(E[] elements){
		this.elements = elements;
	}

	public Number size() {
		return new Number(elements.length);
	}

	public Boolean isEmpty() {
		return Boolean.forPrimitive(elements.length == 0);
	}

	public Boolean contains(E o) {
		for (int i = 0 ; i < elements.length ; i++){
			if (equal(o, elements[i]).value){
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	private Boolean equal(E o, E element){
		return Boolean.forPrimitive(o == null ? element == null : element != null && element.equals(o));
	}

//	public Boolean add(E e) {
//		add(new Number(elements.length), e);
//		return Boolean.TRUE;
//	}
//
//	public Boolean remove(E o) {
//		int i = indexOf(o);
//		if (i > -1){
//			remove(new Number(i));
//			return Boolean.TRUE;
//		}
//		return Boolean.FALSE;
//	}

//	public Boolean containsAll(Collection<E> c) {
//		for (E o : c){
//			if (!contains(o).value){
//				return Boolean.FALSE;
//			}
//		}
//		return Boolean.TRUE;
//	}

//	public Boolean addAll(Collection<E> c) {
//		boolean ret = false;
//		for (E element : c){
//			ret |= add(element).value;
//		}
//		return Boolean.forPrimitive(ret);
//	}
//
//	public Boolean addAll(Number index, Collection<E> c) {
//		E[] initial = elements;
//		for (E element : c){
//			add(index++, element);
//		}
//		return Boolean.forPrimitive(Arrays.equals(elements, initial));
//	}

//	public Boolean removeAll(Collection<E> c) {
//		for (E o : c){
//			remove(o);
//		}
//		return false;
//	}
//
//	public Boolean retainAll(Collection<E> c) {
//		boolean ret = false;
//		for (E element : elements){
//			if (!c.contains(element)){
//				remove(element);
//				ret = true;
//			}
//		}
//		return ret;
//	}
//
//	public void clear() {
//		elements = (E[]) new Object[0];
//	}
//
	public E get(Number index) {
		return elements[index.toInt()];
	}
//
//	public E set(Number index, E element) {
//		E prev = elements[index];
//		elements[index] = element;
//		return prev;
//	}
//
//	public void add(Number index, E element) {
//		E[] newElements = (E[]) new Object[elements.length + 1];
//		System.arraycopy(elements, 0, newElements, 0, index - 1);
//		newElements[index] = element;
//		System.arraycopy(elements, index + 1, newElements, index, elements.length - (index + 1));
//	}
//
//	public E remove(Number index) {
//		E[] newElements = (E[]) new Object[elements.length - 1];
//		System.arraycopy(elements, 0, newElements, 0, index - 1);
//		System.arraycopy(elements, index, newElements, index - 1, elements.length - (index + 1));
//		return elements[index];
//	}
//
//	public int indexOf(E o) {
//		for (int i = 0 ; i < elements.length ; i++){
//			if (equal(o, elements[i]).value){
//				return i;
//			}
//		}
//		return -1;
//	}
//
//	public Number lastIndexOf(E o) {
//		for (int i = elements.length - 1 ; i >= 0 ; i--){
//			if (equal(o, elements[i]).value){
//				return i;
//			}
//		}
//		return -1;
//	}
//
//
//	public List<E> subList(Number fromIndex, Number toIndex) {
//		E[] ret = (E[]) new Object[toIndex - fromIndex];
//		System.arraycopy(elements, fromIndex, ret, 0, ret.length);
//		return new List<E>(ret);
//	}


	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		return Arrays.equals(elements, ((List) o).elements);
	}

	public String toString() {
		return Arrays.toString(elements);
	}
}
