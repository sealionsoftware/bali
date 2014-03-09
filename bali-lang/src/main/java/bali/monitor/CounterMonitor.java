package bali.monitor;

import bali.Count;
import bali.Counter;
import bali.Integer;
import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.number.NumberFactory;

/**
 * User: Richard
 * Date: 07/11/13
 */
@MetaType(Kind.MONITOR)
public class CounterMonitor implements Count {

	private Counter delegate;

	public CounterMonitor() {
		delegate = new Counter(null);
	}

	public CounterMonitor(Integer count) {
		delegate = new Counter(count);
	}

	public synchronized Integer increment() {
		return delegate.increment();
	}

	public synchronized Integer decrement() {
		return delegate.decrement();
	}

	public synchronized Integer value() {
		return delegate.value();
	}
}
