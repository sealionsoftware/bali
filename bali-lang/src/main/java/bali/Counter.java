package bali;

import bali.annotation.Kind;
import bali.annotation.MetaType;

/**
 * User: Richard
 * Date: 07/11/13
 */
@MetaType(Kind.OBJECT)
public class Counter implements Count {

	private int count;

	public Counter() {
		this.count = 0;
	}

	public Counter(Integer count) {
		this.count = _.PRIMITIVE_CONVERTER.from(count);
	}

	public Integer increment() {
		return _.PRIMITIVE_CONVERTER.from(count++);
	}

	public Integer decrement() {
		return _.PRIMITIVE_CONVERTER.from(count--);
	}

	public Integer value() {
		return _.PRIMITIVE_CONVERTER.from(count);
	}
}
