package bali.monitor;

import bali.Boolean;
import bali.Integer;
import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.annotation.Name;
import bali.annotation.Nullable;
import bali.annotation.Parameters;
import bali.collection.Collection;
import bali.collection.LinkedQueue;
import bali.collection.Queue;
import bali.Iterator;
import bali.type.Type;

import static bali.Primitive.convert;

/**
 * TODO: this is a potential source of non-thread safety - unsafe objects passed in
 * might still be accessible on their originating thread, need a better design
 *
 * User: Richard
 * Date: 09 Apr
 */
@MetaType(Kind.MONITOR)
public class BlockingQueue<T> implements Queue<T> {

	private Integer max;
	private Queue<T> queue = new LinkedQueue<>();

	@Parameters
	public BlockingQueue(@Name("T") Type T, @Nullable @Name("max") Integer max) {
		this.max = max;
	}

	public synchronized void push(T object) {
		while(max != null && convert(queue.size().equalTo(max)) || convert(queue.size().greaterThan(max))){
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		queue.push(object);
		notifyAll();
	}

	public synchronized T pop() {
		while(convert(queue.isEmpty())){
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		T ret = queue.pop();
		notifyAll();
		return ret;

	}

	public synchronized Integer size() {
		return queue.size();
	}

	public synchronized Boolean isEmpty() {
		return queue.isEmpty();
	}

	public synchronized T get(@Name("index") Integer index) {
		return queue.get(index);
	}

	public synchronized Collection<T> join(@Name("operand") Collection<T> operand) {
		return queue.join(operand);
	}

	public synchronized Collection<T> head(@Name("index") Integer index) {
		return queue.head(index);
	}

	public synchronized Collection<T> tail(@Name("index") Integer index) {
		return queue.tail(index);
	}

	public synchronized Iterator<T> iterator() {
		return queue.iterator();
	}
}
