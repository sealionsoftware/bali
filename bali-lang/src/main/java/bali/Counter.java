package bali;

import bali.annotation.Kind;
import bali.annotation.MetaType;
import bali.annotation.Name;
import bali.annotation.Nullable;

import static bali.Primitive.convert;

/**
 * User: Richard
 * Date: 07/11/13
 */
@MetaType(Kind.OBJECT)
public class Counter implements Count {

	private int count;

	public Counter(@Nullable @Name("start") Integer count) {
		if (count != null){
			this.count = convert(count);
		}
	}

	public Integer increment() {
		return convert(count++);
	}

	public Integer decrement() {
		return convert(count--);
	}

	public Integer value() {
		return convert(count);
	}
}
