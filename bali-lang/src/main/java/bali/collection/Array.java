package bali.collection;

import bali.Boolean;
import bali.False;
import bali.Integer;
import bali.Iterator;
import bali.True;
import bali.Value;
import bali.annotation.Name;

import java.util.Arrays;

import static bali.Primitive.convert;
import static bali.Boolean.TRUE;
import static bali.Boolean.FALSE;

/**
 * User: Richard
 * Date: 08/05/13
 */
public final class Array<E extends Value<E>> implements ValueCollection<E> {

	private final E[] elements;
	private final E[] empty;

	public Array(E[] elements){
		this.elements = elements;
		this.empty = Arrays.copyOf(elements, 0);
	}

	public Integer size() {
		return convert(elements.length);
	}

	public Boolean isEmpty() {

		return elements.length == 0 ? TRUE : FALSE;
	}

	public Iterator<E> iterator() {

		return new Iterator<E>(){

			int i = 0;

			public Boolean hasNext() {
				return i >= elements.length ? FALSE : TRUE;
			}

			public E next() {
				return elements[i++];
			}

		};
	}

	public E get(Integer index) {
		return elements[convert(index)];
	}

	public Boolean contains(@Name("value") E value) {
		for (E element : elements) {
			if (element.equalTo(value) == TRUE) {
				return TRUE;
			}
		}
		return FALSE;
	}

	public Array<E> join(Collection<E> operand) {

		int aLen = elements.length;
		E[] ret;

		if (operand instanceof Array){
			Array<E> operandArray = (Array<E>) operand;
			int bLen = operandArray.elements.length;
			ret = Arrays.copyOf(elements, aLen + bLen);
			System.arraycopy(operandArray.elements, 0, ret, aLen, bLen);
		} else {
			ret = Arrays.copyOf(elements, aLen + convert(operand.size()));
			Iterator<E> i = operand.iterator();
			int index = aLen;
			while (convert(i.hasNext())){
				ret[index++] = i.next();
			}
		}

		return new Array<>(ret);
	}

	public Collection<E> head(Integer index) {
		int i = convert(index);
		if (i >= elements.length){
			return this;
		}
		return new Array<>(Arrays.copyOfRange(elements, 0, i));
	}

	public Collection<E> tail(Integer index) {
		int i = convert(index);
		if (i >= elements.length){
			return new Array<>(empty);
		}
		return new Array<>(Arrays.copyOfRange(elements, 0, i));
	}

	public Boolean equalTo(ValueCollection<E> operand) {

		if (operand == null){
			return FALSE;
		}

		if (size() != operand.size()){
			return FALSE;
		}

		Iterator<E> i = iterator();
		Iterator<E> j = operand.iterator();

		while (i.hasNext() == TRUE){
			E local = i.next();
			E remote = j.next();
			if (local.equalTo(remote) != TRUE){
				return FALSE;
			}
		}

		return TRUE;
	}

	public Boolean notEqualTo(@Name("operand") ValueCollection<E> operand) {

		if (operand == null){
			return TRUE;
		}

		if (size() != operand.size()){
			return TRUE;
		}

		Iterator<E> i = iterator();
		Iterator<E> j = operand.iterator();

		while (i.hasNext() == TRUE){
			E local = i.next();
			E remote = j.next();
			if (local.notEqualTo(remote) == TRUE){
				return TRUE;
			}
		}

		return FALSE;
	}

	public java.lang.String toString(){
		StringBuilder sb = new StringBuilder("[");
		if (elements.length > 0){
			sb.append(elements[0]);
			for (int i = 1 ; i < elements.length ; i++){
				sb.append(", ").append(elements[i]);
			}
		}
		sb.append("]");
		return sb.toString();
	}
}
