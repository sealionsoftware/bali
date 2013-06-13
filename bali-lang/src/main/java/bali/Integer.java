package bali;

import java.lang.*;

import static bali.Boolean.FALSE;
import static bali.Boolean.TRUE;
import static bali._.NUMBER_FACTORY;

/**
 * User: Richard
 * Date: 11/06/13
 */
class Integer implements Number {

	private static final java.lang.String INVALID_NUMBER_TYPE = "Invalid Number Type";

	final int value;

	Integer(int value) {
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
		return new Integer((short) -value);
	}

	public Boolean greaterThan(Number o) {
		if (o instanceof Integer){
			return greaterThan((Integer) o);
		}
		throw new RuntimeException(INVALID_NUMBER_TYPE);
	}

	public Boolean lessThan(Number o) {
		if (o instanceof Integer){
			return lessThan((Integer) o);
		}
		throw new RuntimeException(INVALID_NUMBER_TYPE);
	}

	public Number add(Number o) {
		if (o instanceof Integer){
			return add((Integer) o);
		}
		if (o instanceof Short){
			return add((Short) o);
		}
		if (o instanceof Byte){
			return add((Byte) o);
		}
		throw new RuntimeException(INVALID_NUMBER_TYPE);
	}

	public Number subtract(Number o) {
		if (o instanceof Integer){
			return subtract((Integer) o);
		}
		throw new RuntimeException(INVALID_NUMBER_TYPE);
	}

	public Number multiply(Number o) {
		if (o instanceof Integer){
			return multiply((Integer) o);
		}
		throw new RuntimeException(INVALID_NUMBER_TYPE);
	}

	public Number divide(Number o) {
		if (o instanceof Integer){
			return divide((Integer) o);
		}
		throw new RuntimeException(INVALID_NUMBER_TYPE);
	}

	public Boolean equalTo(Number o) {
		if (o instanceof Integer){
			return equalTo((Integer) o);
		}
		throw new RuntimeException(INVALID_NUMBER_TYPE);
	}

	public Boolean greaterThan(Integer o) {
		return value > o.value ? TRUE : FALSE;
	}

	public Boolean lessThan(Integer o) {
		return value < o.value ? TRUE : FALSE;
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

	public Number subtract(Integer o) {
		long ret = (long) value - o.value;
		return NUMBER_FACTORY.forLong(ret);
	}

	public Number multiply(Integer o) {
		long ret = (long) value * o.value;
		return NUMBER_FACTORY.forLong(ret);
	}

	public Number divide(Integer o) {
		long ret = (long) value / o.value;
		return NUMBER_FACTORY.forLong(ret);
	}

	public Boolean equalTo(Integer operand) {
		return value == operand.value ? TRUE : FALSE;
	}
}
