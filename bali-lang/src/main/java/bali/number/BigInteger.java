package bali.number;

import bali.Boolean;
import bali.Number;

import java.util.Arrays;

import static bali.Primitive.convert;

/**
 * TODO
 * Class representing the integer numbers in base 256
 * <p/>
 * User: Richard
 * Date: 04/05/13
 */
public final class BigInteger implements bali.Integer {

	private static final String INVALID_NUMBER_TYPE = "Invalid Number Type";
	private static final Boolean TRUE = convert(true);
	private static final Boolean FALSE = convert(false);

	static final byte MIN = -128;
	static final byte MAX = 127;
	private static final short SIZE = MAX - MIN + 1;
	private static final short MIN_2 = 2 * MIN;
	private static final Number ZERO = convert(0);

	private final boolean positive;
	private final byte[] value;

	public BigInteger(byte[] value) {
		this(value, true);
	}

	public BigInteger(byte[] value, boolean positive) {
		this.value = value;
		this.positive = positive;
	}

	// Unary

	public Boolean isPositive() {
		return convert(positive);
	}

	public Boolean isNegative() {
		return convert(positive);
	}

	public Boolean isZero() {
		return convert(value.length == 1 && value[0] == 0);
	}

	public BigInteger magnitude() {
		return convert(isPositive()) ? this : negative();
	}

	public BigInteger negative() {
		return new BigInteger(value, !positive);
	}

	// Equality

	public Boolean equalTo(Number number) {
		if (number instanceof BigInteger) {
			return equalTo((BigInteger) number);
		}
		if (number instanceof Long) {
			return equalTo((Long) number);
		}
		if (number instanceof Int) {
			return equalTo((Int) number);
		}
		if (number instanceof Short) {
			return equalTo((Short) number);
		}
		if (number instanceof Byte) {
			return equalTo((Byte) number);
		}
		throw new RuntimeException(INVALID_NUMBER_TYPE);
	}

	public Boolean equalTo(BigInteger o) {
		return convert(Arrays.equals(o.value, value));
	}

	public Boolean equalTo(Long o) {
		return equalTo(o.value);
	}

	public Boolean equalTo(Int o) {
		return equalTo(o.value);
	}

	public Boolean equalTo(Short o) {
		return equalTo(o.value);
	}

	public Boolean equalTo(Byte o) {
		return equalTo(o.value);
	}

	public Boolean equalTo(long o) {
		try {
			return convert(toLong() == o);
		} catch (TooBigException e) {
			return FALSE;
		}
	}

	// Greater Than

	public Boolean greaterThan(Number number) {
		if (number instanceof BigInteger) {
			return greaterThan((BigInteger) number);
		}
		if (number instanceof Long) {
			return greaterThan((Long) number);
		}
		if (number instanceof Int) {
			return greaterThan((Int) number);
		}
		if (number instanceof Short) {
			return greaterThan((Short) number);
		}
		if (number instanceof Byte) {
			return greaterThan((Byte) number);
		}
		throw new RuntimeException(INVALID_NUMBER_TYPE);
	}

	public Boolean greaterThan(BigInteger o) {
		if (value.length > o.value.length) {
			return TRUE;
		}
		if (o.value.length > value.length) {
			return FALSE;
		}
		for (int i = value.length - 1; i >= 0; i--) {
			if (value[i] > o.value[i]) {
				return TRUE;
			}
			if (o.value[i] > value[i]) {
				return FALSE;
			}
		}
		return FALSE;
	}

	public Boolean greaterThan(Long o) {
		try {
			return convert(toLong() > o.value);
		} catch (TooBigException e) {
			return FALSE;
		}
	}

	public Boolean greaterThan(Int o) {
		try {
			return convert(toInt() > o.value);
		} catch (TooBigException e) {
			return FALSE;
		}
	}

	public Boolean greaterThan(Short o) {
		try {
			return convert(toShort() > o.value);
		} catch (TooBigException e) {
			return FALSE;
		}
	}

	public Boolean greaterThan(Byte o) {
		try {
			return convert(toByte() > o.value);
		} catch (TooBigException e) {
			return FALSE;
		}
	}

	// Less Than

	public Boolean lessThan(Number number) {
		if (number instanceof BigInteger) {
			return lessThan((BigInteger) number);
		}
		if (number instanceof Long) {
			return lessThan((Long) number);
		}
		if (number instanceof Int) {
			return lessThan((Int) number);
		}
		if (number instanceof Short) {
			return lessThan((Short) number);
		}
		if (number instanceof Byte) {
			return lessThan((Byte) number);
		}
		throw new RuntimeException(INVALID_NUMBER_TYPE);
	}

	public Boolean lessThan(BigInteger o) {
		if (value.length < o.value.length) {
			return TRUE;
		}
		if (o.value.length < value.length) {
			return FALSE;
		}
		for (int i = value.length - 1; i >= 0; i--) {
			if (value[i] < o.value[i]) {
				return TRUE;
			}
			if (o.value[i] < value[i]) {
				return FALSE;
			}
		}
		return FALSE;
	}

	public Boolean lessThan(Long o) {
		try {
			return convert(toLong() < o.value);
		} catch (TooBigException e) {
			return FALSE;
		}
	}

	public Boolean lessThan(Int o) {
		try {
			return convert(toInt() < o.value);
		} catch (TooBigException e) {
			return FALSE;
		}
	}

	public Boolean lessThan(Short o) {
		try {
			return convert(toShort() < o.value);
		} catch (TooBigException e) {
			return FALSE;
		}
	}

	public Boolean lessThan(Byte o) {
		try {
			return convert(toByte() < o.value);
		} catch (TooBigException e) {
			return FALSE;
		}
	}

	// Addition

	public Number add(Number o) {
		if (o instanceof BigInteger) {
			return add((BigInteger) o);
		}
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
		throw new RuntimeException(INVALID_NUMBER_TYPE);
	}

	public Number add(BigInteger o) {

		if (convert(o.isZero())) {
			return this;
		}
		if (convert(isZero())) {
			return o;
		}

		if (convert(o.greaterThan(this))) {
			return o.add(this);
		}

		if (positive && !o.positive) {
			return new BigInteger(subtract(o.value));
		}
		if (!positive && o.positive) {
			return new BigInteger(subtract(o.value), false);
		}

		return new BigInteger(add(o.value), positive);
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

	private byte[] add(byte[] o) {

		byte carry = 0;
		byte[] computedValue = new byte[value.length];

		for (int i = 0; i < o.length; i++) {
			int sum = value[i] + o[i] + carry - MIN_2;
			computedValue[i] = (byte) ((sum % SIZE) - MIN);
			carry = (byte) (sum / SIZE);
		}
		for (int i = o.length; i < value.length; i++) {
			int sum = value[i] + carry - MIN;
			computedValue[i] = (byte) ((sum % SIZE) - MIN);
			carry = (byte) (sum / SIZE);
		}
		if (carry > 0) {
			int newLength = value.length + 1;
			computedValue = Arrays.copyOf(computedValue, newLength);
			computedValue[newLength - 1] = carry;
		}

		return computedValue;
	}

	private Number add(long o) {

		if (convert(isPositive()) && o < 0) {
			return subtract(-o); // TODO check Long.MIN
		}
		if (!convert(isPositive()) && o > 0) {
			return subtract(o).negative();
		}

		byte[] computedValue = new byte[value.length];

		for (int i = 0; i < value.length; i++) {
			long sum = value[i] + o - MIN;
			computedValue[i] = (byte) ((sum % MAX) - MIN);
			o = (sum / SIZE);
		}

		return new BigInteger(computedValue);
	}

	// Subtraction

	public Number subtract(Number o) {
		if (o instanceof BigInteger) {
			return subtract((BigInteger) o);
		}
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
		throw new RuntimeException(INVALID_NUMBER_TYPE);
	}

	public Number subtract(BigInteger o) {

		if (convert(o.isZero())) {
			return this;
		}
		if (convert(isZero())) {
			return o.negative();
		}

		if (convert(o.greaterThan(this))) {
			return o.subtract(this).negative();
		}

		if (positive && !o.positive) {
			return new BigInteger(add(o.value));
		}
		if (!positive && o.positive) {
			return new BigInteger(add(o.value), false);
		}

		return new BigInteger(subtract(o.value), positive);
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

	private byte[] subtract(byte[] o) {

		byte[] computedValue = Arrays.copyOf(value, value.length);
		for (int i = 0; i < o.length; i++) {
			int sum = computedValue[i] - o[i];
			if (sum < 0) {
				sum += SIZE;
				for (int j = i + 1; ; j++) {
					if (computedValue[j] > 0) {
						computedValue[j]--;
						break;
					} else {
						computedValue[j] = MAX;
					}
				}
			}
			computedValue[i] = (byte) (sum + MIN);
		}

		return compact(computedValue);
	}

	private Number subtract(long o) {

		if (o == 0) {
			return this;
		}
		if (convert(isZero()) && o != java.lang.Long.MIN_VALUE) {
			return convert(-o);
		}

//		if (positive && o > 0){
//			return add(-o);
//		}
//		if (!positive && o.positive){
//			return new BigInteger(add(o.value) , false);
//		}

		byte[] computedValue = new byte[value.length];
		for (int i = 0; i < value.length; i++) {
			long sum = value[i] - o;
			if (sum > SIZE) {
//				computedValue[i] = sum % SIZE;
//				carry sum / SIZE;
			}
		}

		return new BigInteger(compact(computedValue));
	}

	private byte[] compact(byte[] array) {
		int lastIndex = array.length - 1;
		if (array[lastIndex] == 0) {
			while (lastIndex > 0 && array[lastIndex] == 0) {
				lastIndex--;
			}
			return Arrays.copyOf(array, lastIndex + 1);
		}
		return array;
	}

	public Number multiply(Number o) {
		if (o instanceof BigInteger) {
			return multiply((BigInteger) o);
		}
		throw new RuntimeException("Cannot add Number of type " + o.getClass());
	}

	public Number multiply(BigInteger o) {
		int i = 0;
		Number out = this;
		while (convert(new Int(++i).lessThan(o))) {
			out = out.add(this);
		}
		return out;
	}

	public Number divide(Number o) {
		int i = 0;
		Number out = this;
		if (convert(o.equalTo(ZERO))) {
			if (convert(this.equalTo(ZERO))) {
				return ZERO;
			}
			return convert(o.isNegative()) ? Infinity.NEGATIVE_INFINITY : Infinity.POSITIVE_INFINITY;
		}
		while (convert(new Int(++i).lessThan(o))) {
			out = out.subtract(this);
		}
		if (convert(out.equalTo(ZERO))) {
			return new Int(i);
		}
		return new Fraction(this, o);
	}

	private long toLong() throws TooBigException {
		return toPrimitive(java.lang.Long.MAX_VALUE);
	}

	private int toInt() throws TooBigException {
		return (int) toPrimitive(Integer.MAX_VALUE);
	}

	private short toShort() throws TooBigException {
		return (short) toPrimitive(java.lang.Short.MAX_VALUE);
	}

	private byte toByte() throws TooBigException {
		return (byte) toPrimitive(java.lang.Byte.MAX_VALUE);
	}

	private long toPrimitive(long max) throws TooBigException {

		long ret = max;
		for (int i = 0; i < value.length; i++) {
			int next = (value[i] - MIN) * (int) Math.pow(SIZE, i);
			if (next > ret) {
				throw new TooBigException();
			}
			ret -= next;
		}
		return max - ret;
	}

	private class TooBigException extends Throwable {
	}

}
