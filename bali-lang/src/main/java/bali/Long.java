package bali;

import static bali.Boolean.FALSE;
import static bali.Boolean.TRUE;
import static bali._.NUMBER_FACTORY;

/**
 * User: Richard
 * Date: 11/06/13
 */
class Long implements Number {

	private static final java.lang.String INVALID_NUMBER_TYPE = "Invalid Number Type";

	static final BigInteger POSITIVE_HORIZON = new BigInteger(new byte[]{127,127,127});
	static final BigInteger NEGATIVE_HORIZON = new BigInteger(new byte[]{-127,-127,-127});

	final long value;

	Long(long value) {
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
		return new Long((short) -value);
	}

	public Boolean greaterThan(Number o) {
		if (o instanceof Long){
			return greaterThan((Long) o);
		}
		throw new RuntimeException(INVALID_NUMBER_TYPE);
	}

	public Boolean lessThan(Number o) {
		if (o instanceof Long){
			return lessThan((Long) o);
		}
		throw new RuntimeException(INVALID_NUMBER_TYPE);
	}

	public Number add(Number o) {
		if (o instanceof Long){
			return add((Long) o);
		}
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
		if (o instanceof Long){
			return subtract((Long) o);
		}
		throw new RuntimeException(INVALID_NUMBER_TYPE);
	}

	public Number multiply(Number o) {
		if (o instanceof Long){
			return multiply((Long) o);
		}
		throw new RuntimeException(INVALID_NUMBER_TYPE);
	}

	public Number divide(Number o) {
		if (o instanceof Long){
			return divide((Long) o);
		}
		throw new RuntimeException(INVALID_NUMBER_TYPE);
	}

	public Boolean equalTo(Number o) {
		if (o instanceof Long){
			return equalTo((Long) o);
		}
		throw new RuntimeException(INVALID_NUMBER_TYPE);
	}

	public Boolean greaterThan(Long o) {
		return value > o.value ? TRUE : FALSE;
	}

	public Boolean lessThan(Long o) {
		return value < o.value ? TRUE : FALSE;
	}

	public Number add(Long o) {
		return add(o.value);
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

	private Number add(long o) {
		long rem = java.lang.Long.MAX_VALUE - value;
		if (rem < o){
			return POSITIVE_HORIZON.add(new Long(o - rem));
		}

		rem = java.lang.Long.MIN_VALUE - value;
		if (rem < o){
			return NEGATIVE_HORIZON.add(new Long(o - rem));
		}

		long ret = value + o;
		return NUMBER_FACTORY.forLong(ret);
	}

	public Number subtract(Long o) {
		long ret = value - o.value;
		return NUMBER_FACTORY.forLong(ret);
	}

	public Number multiply(Long o) {
		long ret = value * o.value;
		return NUMBER_FACTORY.forLong(ret);
	}

	public Number divide(Long o) {
		long ret = value / o.value;
		return NUMBER_FACTORY.forLong(ret);
	}

	public Boolean equalTo(Long operand) {
		return value == operand.value ? TRUE : FALSE;
	}
}
