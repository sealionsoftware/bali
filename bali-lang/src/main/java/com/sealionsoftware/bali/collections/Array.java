package com.sealionsoftware.bali.collections;

import bali.*;
import bali.Boolean;
import bali.Number;
import com.sealionsoftware.bali.IdentityBoolean;

import java.util.Arrays;

import static com.sealionsoftware.bali.IdentityBoolean.FALSE;
import static com.sealionsoftware.bali.IdentityBoolean.TRUE;
import static com.sealionsoftware.bali._.NUMBER_FACTORY;

/**
 * User: Richard
 * Date: 08/05/13
 */
public class Array<E> implements bali.Iterable<E>, Value<Array> {

	private E[] elements;

	public Array(E[] elements){
		this.elements = elements;
	}

	public Number size() {
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

	public Boolean equalTo(Array operand) {
		return Arrays.equals(elements, operand.elements) ? IdentityBoolean.TRUE : IdentityBoolean.FALSE;
	}
}
