package bali.collection;

import bali.Boolean;
import bali.number.Integer;
import bali.Iterator;
import bali.Iterable;
import bali.Number;
import bali.IdentityBoolean;
import bali.Value;

import java.util.Arrays;

import static bali.number.NumberFactory.NUMBER_FACTORY;

/**
 * User: Richard
 * Date: 08/05/13
 */
public final class Array<E extends Value<?>> implements Collection<E>, Value<Array<E>> {

	private final E[] elements;

	public Array(E[] elements){
		this.elements = elements;
	}

	public Integer size() {
		return NUMBER_FACTORY.forInt(elements.length);
	}

	public Boolean isEmpty() {
		return elements.length == 0 ? IdentityBoolean.TRUE : IdentityBoolean.FALSE;
	}

	public Iterator<E> iterator() {

		return new Iterator<E>(){

			int i = 0;

			public Boolean hasNext() {
				return i >= elements.length ? IdentityBoolean.FALSE : IdentityBoolean.TRUE;
			}

			public E next() {
				return elements[i++];
			}

		};
	}

	public E get(Number index) {
		return elements[NUMBER_FACTORY.valueOf(index)];
	}

	public Boolean equalTo(Array<E> operand) {
		return Arrays.equals(elements, operand.elements) ? IdentityBoolean.TRUE : IdentityBoolean.FALSE;
	}
}
