package bali.number;

import bali.Boolean;
import bali.Number;
import bali.annotation.MetaType;
import bali.annotation.MetaTypes;

import static bali.IdentityBoolean.FALSE;
import static bali.IdentityBoolean.TRUE;
import static bali.number.NumberFactory.NUMBER_FACTORY;

/**
 * User: Richard
 * Date: 11/06/13
 */
@MetaType(MetaTypes.CLASS)
class Long implements Integer {

	static final long MAX_VALUE = java.lang.Long.MAX_VALUE;
	static final long MIN_VALUE = java.lang.Long.MIN_VALUE;
	static final BigInteger POSITIVE_HORIZON = new BigInteger(new byte[]{-128, -128, -128, -128, -128, -128, -128, -128, -128, -127});
	static final BigInteger NEGATIVE_HORIZON = new BigInteger(new byte[]{-127, -128, -128, -128, -128, -128, -128, -128, -128, -127}, false);

	final long value;

	public Long(long value) {
		this.value = value;
	}

	// Unary

	public Boolean isPositive() {
		return value > 0 ? TRUE : FALSE;
	}

	public Boolean isNegative() {
		return value < 0 ? TRUE : FALSE;
	}

	public Boolean isZero() {
		return value == 0 ? TRUE : FALSE;
	}

	public Number magnitude() {
		return value < 0 ? negative() : this;
	}

	public Number negative() {
		return new Long(-value);
	}

	// Equality

	public Boolean equalTo(Number o) {
		if (o instanceof Long) {
			return equalTo((Long) o);
		}
		if (o instanceof Integer) {
			return equalTo((Integer) o);
		}
		if (o instanceof Short) {
			return equalTo((Short) o);
		}
		if (o instanceof Byte) {
			return equalTo((Byte) o);
		}
		return o.equalTo(this);
	}

	public Boolean equalTo(Long operand) {
		return equalTo(operand.value);
	}

	public Boolean equalTo(Int operand) {
		return equalTo(operand.value);
	}

	public Boolean equalTo(Short operand) {
		return equalTo(operand.value);
	}

	public Boolean equalTo(Byte operand) {
		return equalTo(operand.value);
	}

	public Boolean equalTo(long operand) {
		return value == operand ? TRUE : FALSE;
	}

	// Greater Than

	public Boolean greaterThan(Number o) {
		if (o instanceof Long) {
			return greaterThan((Long) o);
		}
		if (o instanceof Integer) {
			return greaterThan((Integer) o);
		}
		if (o instanceof Short) {
			return greaterThan((Short) o);
		}
		if (o instanceof Byte) {
			return greaterThan((Byte) o);
		}
		return o.lessThan(this).and(equalTo(o).not());
	}

	public Boolean greaterThan(Long o) {
		return greaterThan(o.value);
	}

	public Boolean greaterThan(Int o) {
		return greaterThan(o.value);
	}

	public Boolean greaterThan(Short o) {
		return greaterThan(o.value);
	}

	public Boolean greaterThan(Byte o) {
		return greaterThan(o.value);
	}

	public Boolean greaterThan(long o) {
		return value > o ? TRUE : FALSE;
	}

	// Less Than

	public Boolean lessThan(Number o) {
		if (o instanceof Long) {
			return lessThan((Long) o);
		}
		if (o instanceof Int) {
			return lessThan((Int) o);
		}
		if (o instanceof Short) {
			return lessThan((Short) o);
		}
		if (o instanceof Byte) {
			return lessThan((Byte) o);
		}
		return o.greaterThan(this).and(equalTo(o).not());
	}

	public Boolean lessThan(Long o) {
		return lessThan(o.value);
	}

	public Boolean lessThan(Int o) {
		return lessThan(o.value);
	}

	public Boolean lessThan(Short o) {
		return lessThan(o.value);
	}

	public Boolean lessThan(Byte o) {
		return lessThan(o.value);
	}

	public Boolean lessThan(long o) {
		return value < o ? TRUE : FALSE;
	}

	// Addition

	public Number add(Number o) {
		if (o instanceof Long) {
			return add((Long) o);
		}
		if (o instanceof Int) {
			return add((Int) o);
		}
		if (o instanceof Short) {
			return add((Short) o);
		}
		if (o instanceof Byte) {
			return add((Byte) o);
		}
		return o.add(this);
	}

	public Number add(Long o) {
		return add(o.value);
	}

	public Number add(Int o) {
		return add(o.value);
	}

	public Number add(Short o) {
		return add(o.value);
	}

	public Number add(Byte o) {
		return add(o.value);
	}

	private Number add(long o) {
		long rem = java.lang.Long.MAX_VALUE - value;
		if (rem < o) {
			return POSITIVE_HORIZON.add(new Long(o - rem - 1));
		}

		rem = java.lang.Long.MIN_VALUE + value;
		if (rem < o) {
			return NEGATIVE_HORIZON.subtract(new Long(o - rem));
		}

		long ret = value + o;
		return NUMBER_FACTORY.forLong(ret);
	}


	// Subtraction

	public Number subtract(Number o) {
		if (o instanceof Long) {
			return subtract((Long) o);
		}
		if (o instanceof Int) {
			return subtract((Int) o);
		}
		if (o instanceof Short) {
			return subtract((Short) o);
		}
		if (o instanceof Byte) {
			return subtract((Byte) o);
		}
		return o.subtract(this).negative();
	}

	public Number subtract(Long o) {
		return subtract(o.value);
	}

	public Number subtract(Int o) {
		return subtract(o.value);
	}

	public Number subtract(Short o) {
		return subtract(o.value);
	}

	public Number subtract(Byte o) {
		return subtract(o.value);
	}

	private Number subtract(long o) {
		long rem = java.lang.Long.MIN_VALUE - value;
		if (rem < o) {
			return NEGATIVE_HORIZON.subtract(new Long(o - rem));
		}

		rem = java.lang.Long.MAX_VALUE - value;
		if (rem < o) {
			return POSITIVE_HORIZON.add(new Long(o - rem));
		}

		long ret = value - o;
		return NUMBER_FACTORY.forLong(ret);
	}

	// Multiplication

	public Number multiply(Number o) {
		if (o instanceof Long) {
			return multiply((Long) o);
		}
		if (o instanceof Int) {
			return multiply((Int) o);
		}
		if (o instanceof Short) {
			return multiply((Short) o);
		}
		if (o instanceof Byte) {
			return multiply((Byte) o);
		}
		return o.multiply(this);
	}

	public Number multiply(Long o) {
		return multiply(o.value);
	}

	public Number multiply(Int o) {
		return multiply(o.value);
	}

	public Number multiply(Short o) {
		return multiply(o.value);
	}

	public Number multiply(Byte o) {
		return multiply(o.value);
	}

	private Number multiply(long o) {
		long rem = MAX_VALUE - value;
		if (rem < o) {
			return POSITIVE_HORIZON.add(new Long(o - rem));
		}

		rem = MIN_VALUE + value;
		if (rem < o) {
			return NEGATIVE_HORIZON.add(new Long(o - rem));
		}

		long ret = value * o;
		return NUMBER_FACTORY.forLong(ret);
	}

	// Division

	public Number divide(Number o) {
		if (o instanceof Long) {
			return divide((Long) o);
		}
		if (o instanceof Int) {
			return divide((Int) o);
		}
		if (o instanceof Short) {
			return divide((Short) o);
		}
		if (o instanceof Byte) {
			return divide((Byte) o);
		}
		return o.divide(this);
	}

	public Number divide(Long o) {
		return divide(o.value);
	}

	public Number divide(Int o) {
		return divide(o.value);
	}

	public Number divide(Short o) {
		return divide(o.value);
	}

	public Number divide(Byte o) {
		return divide(o.value);
	}

	private Number divide(long o) {
		long rem = MAX_VALUE - value;
		if (rem < o) {
			return POSITIVE_HORIZON.add(new Long(o - rem));
		}

		rem = MIN_VALUE + value;
		if (rem < o) {
			return NEGATIVE_HORIZON.add(new Long(o - rem));
		}

		long ret = value / o;
		return NUMBER_FACTORY.forLong(ret);
	}
}
