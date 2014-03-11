package bali.time.standard;

import bali.Boolean;
import bali.Number;
import bali.annotation.Name;
import bali.time.Instant;
import bali.time.Interval;

/**
 * Represents an instant in universal time as milliseconds since the start of 1970 UTC
 *
 * User: Richard
 * Date: 11/07/13
 */
public final class StandardInstant implements Instant {

	private Number value;

	public StandardInstant(Number value) {
		this.value = value;
	}

	public Instant add(Interval interval){
		return new StandardInstant(value.add(interval.getTimeInMillis()));
	}

	public Instant subtract(Interval interval){
		return new StandardInstant(value.subtract(interval.getTimeInMillis()));
	}

	public Boolean greaterThan(Instant o) {
		return value.greaterThan(o.getMillisSince1970());
	}

	public Boolean lessThan(Instant o) {
		return value.lessThan(o.getMillisSince1970());
	}

	public Boolean equalTo(Instant o) {
		return value.equalTo(o.getMillisSince1970());
	}

	public Boolean notEqualTo(@Name("operand") Instant operand) {
		return equalTo(operand).not();
	}

	public Number getMillisSince1970() {
		return value;
	}
}
