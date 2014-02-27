package bali.collection;

import bali.Boolean;
import bali.False;
import bali.Integer;
import bali.Iterator;
import bali.Number;
import bali.True;
import bali.Value;
import bali.annotation.Name;

import java.util.Arrays;

import static bali.number.NumberFactory.NUMBER_FACTORY;

/**
 * User: Richard
 * Date: 08/05/13
 */
public final class Array<E extends Value<E>> implements ValueCollection<E> {

	private final E[] elements;

	public Array(E[] elements){
		this.elements = elements;
	}

	public Integer size() {
		return NUMBER_FACTORY.forInt(elements.length);
	}

	public Boolean isEmpty() {

		return elements.length == 0 ? True.TRUE : False.FALSE;
	}

	public Iterator<E> iterator() {

		return new Iterator<E>(){

			int i = 0;

			public Boolean hasNext() {
				return i >= elements.length ? False.FALSE : True.TRUE;
			}

			public E next() {
				return elements[i++];
			}

		};
	}

	public E get(Number index) {
		return elements[NUMBER_FACTORY.valueOf(index)];
	}

	public Boolean contains(@Name("value") E value) {
		for (E element : elements) {
			if (element.equalTo(value) == True.TRUE) {
				return True.TRUE;
			}
		}
		return False.FALSE;
	}

	public Boolean equalTo(ValueCollection<E> operand) {

		if (operand == null){
			return False.FALSE;
		}

		if (operand instanceof Array){
			return Arrays.equals(elements, ((Array) operand).elements) ? True.TRUE : False.FALSE;
		}

		if (size() != operand.size()){
			return False.FALSE;
		}

		Iterator<E> i = iterator();
		Iterator<E> j = operand.iterator();

		while (i.hasNext() == True.TRUE){
			E local = i.next();
			E remote = j.next();
			if (local.equalTo(remote) != True.TRUE){
				return False.FALSE;
			}
		}

		return True.TRUE;
	}
}
