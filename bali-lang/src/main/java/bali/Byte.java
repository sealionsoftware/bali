package bali;

import static bali.Boolean.FALSE;
import static bali.Boolean.TRUE;
import static bali._.NUMBER_FACTORY;

/**
 * User: Richard
 * Date: 11/06/13
 */
public class Byte implements Number {

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
		return value == 0 ? TRUE : Boolean.FALSE;
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
		return value > o.value ? Boolean.TRUE : Boolean.FALSE;
	}

	// Less than

	public Boolean lessThan(Number o) {
		if (o instanceof Byte) {
			return lessThan((Byte) o);
		}
		return o.greaterThan(this).not().and(equalTo(o).not());
	}

	public Boolean lessThan(Byte o) {
		return value < o.value ? Boolean.TRUE : Boolean.FALSE;
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
		return NUMBER_FACTORY.forInt(ret);
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
		return NUMBER_FACTORY.forInt(ret);
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
		return NUMBER_FACTORY.forInt(ret);
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
		return NUMBER_FACTORY.forInt(ret);
	}
}
