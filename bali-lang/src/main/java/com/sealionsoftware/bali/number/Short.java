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
public class Short implements bali.Number {

	final short value;

	public Short(short value) {
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
		return new Short((short) -value);
	}

	// Equality

	public Boolean equalTo(Number o) {
		if (o instanceof Short) {
			return equalTo((Short) o);
		}
		if (o instanceof Byte) {
			return equalTo((Byte) o);
		}
		return o.equalTo(this);
	}

	public Boolean equalTo(Short operand) {
		return value == operand.value ? TRUE : FALSE;
	}

	public Boolean equalTo(Byte operand) {
		return value == operand.value ? TRUE : FALSE;
	}

	// Greater Than

	public Boolean greaterThan(Number o) {
		if (o instanceof Short) {
			return greaterThan((Short) o);
		}
		if (o instanceof Byte) {
			return greaterThan((Byte) o);
		}
		return o.lessThan(this).not().and(equalTo(o).not());
	}

	public Boolean greaterThan(Short o) {
		return value > o.value ? TRUE : FALSE;
	}

	public Boolean greaterThan(Byte o) {
		return value > o.value ? TRUE : FALSE;
	}

	// Less Than

	public Boolean lessThan(Number o) {
		if (o instanceof Short) {
			return greaterThan((Short) o);
		}
		if (o instanceof Byte) {
			return greaterThan((Byte) o);
		}
		return o.lessThan(this).not().and(equalTo(o).not());
	}

	public Boolean lessThan(Short o) {
		return value < o.value ? TRUE : FALSE;
	}

	public Boolean lessThan(Byte o) {
		return value < o.value ? TRUE : FALSE;
	}

	// Addition

	public Number add(Number o) {
		if (o instanceof Short) {
			return add((Short) o);
		}
		if (o instanceof Byte) {
			return add((Byte) o);
		}
		return o.add(this);
	}

	public Number add(Short o) {
		return add(o.value);
	}

	public Number add(Byte o) {
		return add(o.value);
	}

	private Number add(short o) {
		int ret = value + o;
		return NUMBER_FACTORY.forInt(ret);
	}

	// Subtraction

	public Number subtract(Number o) {
		if (o instanceof Short) {
			return subtract((Short) o);
		}
		if (o instanceof Byte) {
			return subtract((Byte) o);
		}
		return o.subtract(this).negative();
	}

	public Number subtract(Short o) {
		return subtract(o.value);
	}

	public Number subtract(Byte o) {
		return subtract(o.value);
	}

	public Number subtract(short o) {
		int ret = value - o;
		return NUMBER_FACTORY.forInt(ret);
	}

	// Multiplication

	public Number multiply(Number o) {
		if (o instanceof Short) {
			return multiply((Short) o);
		}
		if (o instanceof Byte) {
			return multiply((Byte) o);
		}
		return o.multiply(this);
	}

	public Number multiply(Short o) {
		return multiply(o.value);
	}

	public Number multiply(Byte o) {
		return multiply(o.value);
	}

	public Number multiply(short o) {
		int ret = value * o;
		return NUMBER_FACTORY.forInt(ret);
	}

	// Division

	public Number divide(Number o) {
		if (o instanceof Short) {
			return divide((Short) o);
		}
		if (o instanceof Byte) {
			return divide((Byte) o);
		}
		return o.divide(this);
	}

	public Number divide(Short o) {
		return divide(o.value);
	}

	public Number divide(Byte o) {
		return divide(o.value);
	}

	public Number divide(short o) {
		int ret = value / o;
		return NUMBER_FACTORY.forInt(ret);
	}


}
