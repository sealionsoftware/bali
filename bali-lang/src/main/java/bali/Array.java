package bali;

import java.util.Arrays;

import static bali._.NUMBER_FACTORY;

/**
 * User: Richard
 * Date: 08/05/13
 */
public class Array<E> implements Iterable<E>, Value<Array> {

	private E[] elements;

	public Array(E[] elements){
		this.elements = elements;
	}

	public Number size() {
		return NUMBER_FACTORY.forInt(elements.length);
	}

	public Boolean isEmpty() {
		return elements.length == 0 ? Boolean.TRUE : Boolean.FALSE;
	}

	public Iterator<E> iterator() {

		return new Iterator<E>(){

			int i = 0;

			public Boolean hasNext() {
				return i >= elements.length ? Boolean.FALSE : Boolean.TRUE;
			}

			public E next() {
				return elements[i++];
			}

		};
	}

	public E get(Number index) {
		return elements[NUMBER_FACTORY.valueOf(index)];
	}

	public Boolean equalTo(Array operand) {
		return Arrays.equals(elements, operand.elements) ? Boolean.TRUE : Boolean.FALSE;
	}
}
