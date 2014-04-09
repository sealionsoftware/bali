package bali.collection;

import bali.Iterator;
import bali.Boolean;
import bali.Integer;
import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.annotation.Name;

import static bali.Primitive.convert;
import static bali.number._.ONE;

/**
 * User: Richard
 * Date: 09 Apr
 */
@MetaType(Kind.OBJECT)
public class LinkedQueue<T> implements Queue<T> {

	private List<T> list = new LinkedList<>();

	public void push(T object) {
		list.add(object);
	}

	public T pop() {
		if (convert(list.isEmpty())){
			throw new RuntimeException("The queue is empty");
		}
		T ret = list.get(ONE);
		list.remove(ONE);
		return ret;
	}

	public Integer size() {
		return list.size();
	}

	public Boolean isEmpty() {
		return list.isEmpty();
	}

	public T get(@Name("index") Integer index) {
		return list.get(index);
	}

	public Collection<T> join(@Name("operand") Collection<T> operand) {
		return list.join(operand);
	}

	public Collection<T> head(@Name("index") Integer index) {
		return list.head(index);
	}

	public Collection<T> tail(@Name("index") Integer index) {
		return list.tail(index);
	}

	public Iterator<T> iterator() {
		return list.iterator();
	}
}



