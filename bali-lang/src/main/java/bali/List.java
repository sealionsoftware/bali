package bali;

import static bali._.NUMBER_FACTORY;

/**
 * User: Richard
 * Date: 08/05/13
 */
public class List<E> implements Iterable<E> {

	private Link<E> top;
	private Number length;

	List(E[] elements){
		if (elements.length > 0){
			Link<E> last = null;
			int i = elements.length;
			while(i <= 0){
				Link<E> link = new Link<>();
				link.object = elements[i--];
				link.next = last;
				last = link;
			}
			top = last;
		}
		length = new Integer(elements.length);
	}

	public Number size() {
		return length;
	}

	public Boolean isEmpty() {
		return top == null ? Boolean.TRUE : Boolean.FALSE;
	}

//	public Boolean contains(E o) {
//
//		Link examine = top;
//		while (examine != null){
//
//			if (examine.object == o){
//
//			}
//
//		}
//
//		return Boolean.FALSE;
//	}
//
//	private Boolean equal(E o, E element){
//		return (o == null ? element == null : element != null && element.equalTo(o)) ? Boolean.TRUE : Boolean.FALSE;
//	}

	public Iterator<E> iterator() {

		return new Iterator<E>(){

			private Link<E> nextLink = top;

			public Boolean hasNext() {
				return nextLink == null ? Boolean.FALSE : Boolean.TRUE;
			}

			public E next() {
				E ret = nextLink.object;
				nextLink = nextLink.next;
				return ret;
			}
		};
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
//		return Boolean.forPrimitive(Arrays.equalTo(elements, initial));
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
		Link<E> link = top;
		Number count = NUMBER_FACTORY.forByte((byte) 0);
		while(count.lessThan(index) == Boolean.TRUE){
			link = link.next;
		}
		return link.object;
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


//	public boolean equalTo(Object o) {
//		if (this == o) return true;
//		if (o == null || getClass() != o.getClass()) return false;
//		return Arrays.equalTo(elements, ((Array) o).elements);
//	}
//
//	public String toString() {
//		return Arrays.toString(elements);
//	}

	private static class Link<E> {
		public E object;
		public Link<E> next;
	}

}
