package com.sealionsoftware.bali.number;

import bali.Boolean;
import bali.Number;

import static com.sealionsoftware.bali.IdentityBoolean.FALSE;
import static com.sealionsoftware.bali.IdentityBoolean.TRUE;
import static com.sealionsoftware.bali._.NUMBER_FACTORY;

/**
 * User: Richard
 * Date: 11/06/13
 */
public class Integer implements bali.Number {

	final int value;

	public Integer(int value) {
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
		return new Integer(-value);
	}

	// Equality

	public Boolean equalTo(Number o) {
		if (o instanceof Integer) {
			return equalTo((Integer) o);
		}
		if (o instanceof com.sealionsoftware.bali.number.Short) {
			return equalTo((Short) o);
		}
		if (o instanceof Byte) {
			return equalTo((Byte) o);
		}
		return o.equalTo(this);
	}

	public Boolean equalTo(Integer operand) {
		return equalTo(operand.value);
	}

	public Boolean equalTo(Short operand) {
		return equalTo(operand.value);
	}

	public Boolean equalTo(Byte operand) {
		return equalTo(operand.value);
	}

	public Boolean equalTo(int operand) {
		return value == operand ? TRUE : FALSE;
	}

	// Greater Than

	public Boolean greaterThan(Number o) {
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

	public Boolean greaterThan(Integer o) {
		return greaterThan(o.value);
	}

	public Boolean greaterThan(Short o) {
		return greaterThan(o.value);
	}

	public Boolean greaterThan(Byte o) {
		return greaterThan(o.value);
	}

	public Boolean greaterThan(int o) {
		return value > o ? TRUE : FALSE;
	}

	// Less Than

	public Boolean lessThan(Number o) {
		if (o instanceof Integer) {
			return lessThan((Integer) o);
		}
		if (o instanceof Short) {
			return lessThan((Short) o);
		}
		if (o instanceof Byte) {
			return lessThan((Byte) o);
		}
		return o.greaterThan(this).and(equalTo(o).not());
	}

	public Boolean lessThan(Integer o) {
		return lessThan(o.value);
	}

	public Boolean lessThan(Short o) {
		return lessThan(o.value);
	}

	public Boolean lessThan(Byte o) {
		return lessThan(o.value);
	}

	public Boolean lessThan(int o) {
		return value < o ? TRUE : FALSE;
	}

	// Addition

	public Number add(Number o) {
		if (o instanceof Integer) {
			return add((Integer) o);
		}
		if (o instanceof Short) {
			return add((Short) o);
		}
		if (o instanceof Byte) {
			return add((Byte) o);
		}
		return o.add(this);
	}

	public Number add(Integer o) {
		return add(o.value);
	}

	public Number add(Short o) {
		return add(o.value);
	}

	public Number add(Byte o) {
		return add(o.value);
	}

	private Number add(int o) {
		long ret = (long) value + o;
		return NUMBER_FACTORY.forLong(ret);
	}

	// Subtraction

	public Number subtract(Number o) {
		if (o instanceof Integer) {
			return subtract((Integer) o);
		}
		if (o instanceof Short) {
			return subtract((Short) o);
		}
		if (o instanceof Byte) {
			return subtract((Byte) o);
		}
		return o.subtract(this).negative();
	}

	public Number subtract(Integer o) {
		return subtract(o.value);
	}

	public Number subtract(Short o) {
		return subtract(o.value);
	}

	public Number subtract(Byte o) {
		return subtract(o.value);
	}

	private Number subtract(int o) {
		long ret = (long) value - o;
		return NUMBER_FACTORY.forLong(ret);
	}

	// Multiplication

	public Number multiply(Number o) {
		if (o instanceof Integer) {
			return multiply((Integer) o);
		}
		if (o instanceof Short) {
			return multiply((Short) o);
		}
		if (o instanceof Byte) {
			return multiply((Byte) o);
		}
		return o.multiply(this).negative();
	}

	public Number multiply(Integer o) {
		return multiply(o.value);
	}

	public Number multiply(Short o) {
		return multiply(o.value);
	}

	public Number multiply(Byte o) {
		return multiply(o.value);
	}

	private Number multiply(int o) {
		long ret = (long) value * o;
		return NUMBER_FACTORY.forLong(ret);
	}

	// Division

	public Number divide(Number o) {
		if (o instanceof Integer) {
			return divide((Integer) o);
		}
		if (o instanceof Short) {
			return divide((Short) o);
		}
		if (o instanceof Byte) {
			return divide((Byte) o);
		}
		return o.divide(this);
	}

	public Number divide(Integer o) {
		return divide(o.value);
	}

	public Number divide(Short o) {
		return divide(o.value);
	}

	public Number divide(Byte o) {
		return divide(o.value);
	}

	private Number divide(int o) {
		int ret = value / o;
		return NUMBER_FACTORY.forInt(ret);
	}

}
