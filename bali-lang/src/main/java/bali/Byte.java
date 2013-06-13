package bali;

import static bali.Boolean.TRUE;
import static bali.Boolean.FALSE;
import static bali._.NUMBER_FACTORY;

/**
 * User: Richard
 * Date: 11/06/13
 */
class Byte implements Number {

	private static final java.lang.String INVALID_NUMBER_TYPE = "Invalid Number Type";

	final byte value;

	Byte(byte value) {
		this.value = value;
	}

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
		return value < 0 ? inverse() : this;
	}

	public Number inverse() {
		return new Byte((byte) -value);
	}

	public Boolean greaterThan(Number o) {
		if (o instanceof Byte){
			return value > ((Byte) o).value ? Boolean.TRUE : Boolean.FALSE;
		}
		throw new RuntimeException(INVALID_NUMBER_TYPE);
	}

	public Boolean lessThan(Number o) {
		if (o instanceof Byte){
			return value < ((Byte) o).value ? Boolean.TRUE : Boolean.FALSE;
		}
		throw new RuntimeException(INVALID_NUMBER_TYPE);
	}

	public Number add(Number o) {
		if (o instanceof Byte){
			return add((Byte) o);
		}
		return o.add(this);
	}

	public Number subtract(Number o) {
		if (o instanceof Byte){
			return subtract((Byte) o);
		}
		throw new RuntimeException(INVALID_NUMBER_TYPE);
	}

	public Number multiply(Number o) {
		if (o instanceof Byte){
			return multiply((Byte) o);
		}
		throw new RuntimeException(INVALID_NUMBER_TYPE);
	}

	public Number divide(Number o) {
		if (o instanceof Byte){
			return divide((Byte) o);
		}
		throw new RuntimeException(INVALID_NUMBER_TYPE);
	}

	public Boolean equalTo(Number o) {
		if (o instanceof Byte){
			return equalTo((Byte) o);
		}
		throw new RuntimeException(INVALID_NUMBER_TYPE);
	}

	private Number add(Byte o) {
		int ret = value + o.value;
		return NUMBER_FACTORY.forInt(ret);
	}

	public Number subtract(Byte o) {
		int ret = value - o.value;
		return NUMBER_FACTORY.forInt(ret);
	}

	public Number multiply(Byte o) {
		int ret = value * o.value;
		return NUMBER_FACTORY.forInt(ret);
	}

	public Number divide(Byte o) {
		int ret = value / o.value;
		return NUMBER_FACTORY.forInt(ret);
	}

	public Boolean equalTo(Byte operand) {
		return value == operand.value ? TRUE : FALSE;
	}
}
