package bali.number;

import bali.Boolean;
import bali.Number;

import static bali.False.FALSE;
import static bali.True.TRUE;
import static bali.Primitive.convert;

/**
 * User: Richard
 * Date: 11/06/13
 */
public final class Byte implements bali.Integer {

	static final byte MAX_VALUE = java.lang.Byte.MAX_VALUE;
	static final byte MIN_VALUE = java.lang.Byte.MIN_VALUE;

	final byte value;

	public Byte(byte value) {
		this.value = value;
	}

	// Unary Operations

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
		return new Byte((byte) -value);
	}

	// Equality

	public Boolean equalTo(Number o) {
		if (o instanceof Byte) {
			return equalTo((Byte) o);
		}
		return o.equalTo(this);
	}

	public Boolean equalTo(Byte operand) {
		return value == operand.value ? TRUE : FALSE;
	}

	// Greater than

	public Boolean greaterThan(Number o) {
		if (o instanceof Byte) {
			return greaterThan((Byte) o);
		}
		return o.lessThan(this).not().and(equalTo(o).not());
	}

	public Boolean greaterThan(Byte o) {
		return value > o.value ? TRUE : FALSE;
	}

	// Less than

	public Boolean lessThan(Number o) {
		if (o instanceof Byte) {
			return lessThan((Byte) o);
		}
		return o.greaterThan(this).not().and(equalTo(o).not());
	}

	public Boolean lessThan(Byte o) {
		return value < o.value ? TRUE : FALSE;
	}

	// Addition

	public Number add(Number o) {
		if (o instanceof Byte) {
			return add((Byte) o);
		}
		return o.add(this);
	}

	public Number add(Byte o) {
		int ret = value + o.value;
		return convert(ret);
	}

	// Subtraction

	public Number subtract(Number o) {
		if (o instanceof Byte) {
			return subtract((Byte) o);
		}
		return o.subtract(this);
	}

	public Number subtract(Byte o) {
		int ret = value - o.value;
		return convert(ret);
	}

	// Multiplication

	public Number multiply(Number o) {
		if (o instanceof Byte) {
			return multiply((Byte) o);
		}
		return o.multiply(this);
	}

	public Number multiply(Byte o) {
		int ret = value * o.value;
		return convert(ret);
	}

	// Division

	public Number divide(Number o) {
		if (o instanceof Byte) {
			return divide((Byte) o);
		}
		return o.divide(this);
	}

	public Number divide(Byte o) {
		int ret = value / o.value;
		return convert(ret);
	}
}
