package bali;

import static bali.Boolean.TRUE;
import static bali.Boolean.FALSE;
import static bali._.NUMBER_FACTORY;

/**
 * User: Richard
 * Date: 11/06/13
 */
class Short implements Number {

	private static final java.lang.String INVALID_NUMBER_TYPE = "Invalid Number Type";

	final short value;

	Short(short value) {
		this.value = value;
	}

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
		return value < 0 ? inverse() : this;
	}

	public Number inverse() {
		return new Short((short) -value);
	}

	public Boolean greaterThan(Number o) {
		if (o instanceof Short){
			return greaterThan((Short) o);
		}
		throw new RuntimeException(INVALID_NUMBER_TYPE);
	}

	public Boolean lessThan(Number o) {
		if (o instanceof Short){
			return lessThan((Short) o);
		}
		throw new RuntimeException(INVALID_NUMBER_TYPE);
	}

	public Number add(Number o) {
		if (o instanceof Short){
			return add((Short) o);
		}
		if (o instanceof Byte){
			return add((Byte) o);
		}
		throw new RuntimeException(INVALID_NUMBER_TYPE);
	}

	public Number subtract(Number o) {
		if (o instanceof Short){
			return subtract((Short) o);
		}
		throw new RuntimeException(INVALID_NUMBER_TYPE);
	}

	public Number multiply(Number o) {
		if (o instanceof Short){
			return multiply((Short) o);
		}
		throw new RuntimeException(INVALID_NUMBER_TYPE);
	}

	public Number divide(Number o) {
		if (o instanceof Short){
			return divide((Short) o);
		}
		throw new RuntimeException(INVALID_NUMBER_TYPE);
	}

	public Boolean equalTo(Number o) {
		if (o instanceof Short){
			return equalTo((Short) o);
		}
		throw new RuntimeException(INVALID_NUMBER_TYPE);
	}

	public Boolean greaterThan(Short o) {
		return value > o.value ? TRUE : FALSE;
	}

	public Boolean lessThan(Short o) {
		return value < o.value ? TRUE : FALSE;
	}

	public Number add(Short o) {
		return add(o.value);
	}

	public Number add(Byte o) {
		return add(o.value);
	}

	private Number add(short o){
		int ret = value + o;
		return NUMBER_FACTORY.forInt(ret);
	}

	public Number subtract(Short o) {
		int ret = value - o.value;
		return NUMBER_FACTORY.forInt(ret);
	}

	public Number multiply(Short o) {
		int ret = value * o.value;
		return NUMBER_FACTORY.forInt(ret);
	}

	public Number divide(Short o) {
		int ret = value / o.value;
		return NUMBER_FACTORY.forInt(ret);
	}

	public Boolean equalTo(Short operand) {
		return value == operand.value ? TRUE : FALSE;
	}
}
