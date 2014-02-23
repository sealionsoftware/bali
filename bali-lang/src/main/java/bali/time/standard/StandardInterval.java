package bali.time.standard;

import bali.Boolean;
import bali.Number;
import bali.annotation.MetaType;
import bali.annotation.Kind;
import bali.time.Interval;

/**
 * User: Richard
 * Date: 11/07/13
 */
@MetaType(Kind.OBJECT)
public final class StandardInterval implements Interval {

	private final Number value;

	public StandardInterval(Number value) {
		this.value = value;
	}

	public Boolean greaterThan(Interval o) {
		return value.greaterThan(o.getTimeInMillis());
	}

	public Boolean lessThan(Interval o) {
		return value.lessThan(o.getTimeInMillis());
	}

	public Boolean equalTo(Interval o) {
		return value.equalTo(o.getTimeInMillis());
	}

	public Number getTimeInMillis() {
		return value;
	}
}
