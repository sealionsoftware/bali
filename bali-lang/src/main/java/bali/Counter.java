package bali;

import bali.annotation.MetaType;
import bali.annotation.MetaTypes;
import bali.number.NumberFactory;

/**
 * User: Richard
 * Date: 07/11/13
 */
@MetaType(MetaTypes.CLASS)
public class Counter implements Count {

	private int count;

	public Counter() {
		this.count = 0;
	}

	public Counter(Integer count) {
		this.count = NumberFactory.NUMBER_FACTORY.valueOf(count);
	}

	public Integer increment() {
		return NumberFactory.NUMBER_FACTORY.forInt(count++);
	}

	public Integer decrement() {
		return NumberFactory.NUMBER_FACTORY.forInt(count--);
	}

	public Integer value() {
		return NumberFactory.NUMBER_FACTORY.forInt(count);
	}
}
