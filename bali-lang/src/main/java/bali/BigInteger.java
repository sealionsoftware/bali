package bali;

import java.util.Arrays;

/**
 * Class representing the integer numbers
 * <p/>
 * User: Richard
 * Date: 04/05/13
 */
public class BigInteger implements Number  {

	private static final short MAX_BYTE_SIZE = 0b10000000;
	private static final BigInteger ZERO = new BigInteger(new byte[]{0});

	final byte[] value;

	BigInteger(byte[] value) {
		this.value = value;
	}

//	BigInteger(int value) {
//		byte[] computedValue = new byte[1 + (value / MAX_BYTE_SIZE)];
//
//		int rem = value;
//		int i = 0;
//		while (rem >= MAX_BYTE_SIZE) {
//			computedValue[i++] = (byte) (rem % MAX_BYTE_SIZE);
//			rem /= MAX_BYTE_SIZE;
//		}
//		computedValue[i] = (byte) rem;
//		this.value = compact(computedValue);
//	}

//	int toInt() {
//		int ret = 0;
//		for (int i = 0; i < value.length; i++) {
//			ret += value[i] * Math.pow(MAX_BYTE_SIZE, i);
//		}
//		return ret;
//	}

	public Number add(Number o) {
		if (o instanceof BigInteger){
			return add((BigInteger) o);
		}
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
		throw new RuntimeException("Cannot add Number of type " + o.getClass());
	}

	public Number subtract(Number o) {
		if (o instanceof BigInteger){
			return subtract((BigInteger) o);
		}
		throw new RuntimeException("Cannot subtract Number of type " + o.getClass());
	}

	public Boolean greaterThan(Number o) {
		if (o instanceof BigInteger){
			return greaterThan((BigInteger) o);
		}
		throw new RuntimeException("Cannot compare Number of type " + o.getClass());
	}

	public Boolean lessThan(Number o) {
		if (o instanceof BigInteger){
			return lessThan((BigInteger) o);
		}
		throw new RuntimeException("Cannot compare Number of type " + o.getClass());
	}

	public BigInteger add(BigInteger o) {

		if (o.isZero() == Boolean.TRUE) {
			return this;
		}
		if (isZero() == Boolean.TRUE) {
			return o;
		}

		if (isPositive().and(o.isNegative()) == Boolean.TRUE) {
			return this.subtract(o.inverse());
		} else if (o.isPositive().and(isNegative()) == Boolean.TRUE) {
			return o.subtract(this.inverse());
		}

		int length;
		byte[] one;
		byte[] two;

		if (value.length == o.value.length) {
			length = value.length;
			one = value;
			two = o.value;
		} else if (value.length > o.value.length) {
			length = value.length;
			one = value;
			two = Arrays.copyOf(o.value, length);
		} else {
			length = o.value.length;
			one = Arrays.copyOf(value, length);
			two = o.value;
		}

		byte carry = 0;
		byte[] computedValue = new byte[length];

		for (int i = 0; i < length; i++) {
			int sum = one[i] + two[i] + carry;
			if (sum == 0) {
				computedValue[i] = 0;
				carry = (byte) 0;
			} else {
				computedValue[i] = (byte) (sum % MAX_BYTE_SIZE);
				carry = (byte) (sum / MAX_BYTE_SIZE);
			}
		}

		if (carry != 0) {
			computedValue = Arrays.copyOf(computedValue, computedValue.length + 1);
			computedValue[computedValue.length - 1] = carry;
		} else {
			computedValue = compact(computedValue);
		}

		return new BigInteger(computedValue);
	}

	public BigInteger add(Long o) {
		return add(o.value);
	}

	public BigInteger add(Integer o) {
		return add(o.value);
	}

	public BigInteger add(Short o) {
		return add(o.value);
	}

	public BigInteger add(Byte o) {
		return add(o.value);
	}

	private BigInteger add(long o) {
		return null;
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

	public BigInteger subtract(BigInteger o) {

		if (o.isZero() == Boolean.TRUE) {
			return this;
		}
		if (isZero() == Boolean.TRUE) {
			return o;
		}
		if (isNegative().and(o.isPositive()) == Boolean.TRUE || isPositive().and(o.isNegative()) == Boolean.TRUE) {
			return this.add(o.inverse());
		}
		if (equals(o) == Boolean.TRUE) {
			return ZERO;
		}

		boolean flip;
		int length;
		byte[] one;
		byte[] two;

		if (magnitude().greaterThan(o.magnitude()) == Boolean.TRUE) {
			flip = false;
			length = value.length;
			one = Arrays.copyOf(value, length);
			two = value.length > o.value.length ? Arrays.copyOf(o.value, length) : o.value;
		} else {
			flip = true;
			length = o.value.length;
			one = Arrays.copyOf(o.value, length);
			two = o.value.length > value.length ? Arrays.copyOf(value, length) : value;
		}

		if (isNegative().and(o.isNegative()) == Boolean.TRUE) {
			flip = !flip;
			invert(one);
			invert(two);
		}

		byte[] computedValue = new byte[length];

		for (int i = 0; i < length; i++) {
			byte diff = (byte) (one[i] - two[i]);
			if (diff < 0) {
				int j = i;
				while (true) {
					if (one[j] == 0) {
						one[j] = 127;
					} else {
						one[j]--;
						break;
					}
					j++;
				}
				computedValue[i] = (byte) (128 + diff);
			} else {
				computedValue[i] = diff;
			}
		}

		computedValue = compact(computedValue);

		if (flip) {
			invert(computedValue);
		}

		return new BigInteger(computedValue);
	}

	public Boolean isPositive() {
		return (value[value.length - 1] > 0) ? Boolean.TRUE : Boolean.FALSE;
	}

	public Boolean isNegative() {
		return (value[value.length - 1] < 0) ? Boolean.TRUE : Boolean.FALSE;
	}

	public Boolean isZero() {
		return (value.length == 1 && value[0] == 0) ? Boolean.TRUE : Boolean.FALSE;
	}

	public BigInteger magnitude() {
		return isPositive() == Boolean.TRUE ? this : inverse();
	}

	public BigInteger inverse() {
		byte[] ret = Arrays.copyOf(value, value.length);
		invert(ret);
		return new BigInteger(ret);
	}

	private void invert(byte[] in) {
		for (int i = 0; i < in.length; i++) {
			in[i] = (byte) -in[i];
		}
	}

	public Boolean equals(BigInteger o) {
		return Arrays.equals(o.value, value) ? Boolean.TRUE : Boolean.FALSE;
	}

	public Boolean equalTo(Number number) {
		if (number instanceof BigInteger){
			return equals((BigInteger) number);
		}
		return Boolean.FALSE;
	}

	public Boolean greaterThan(BigInteger o) {
		if (value.length > o.value.length) {
			return Boolean.TRUE;
		}
		if (o.value.length > value.length) {
			return Boolean.FALSE;
		}
		for (int i = value.length - 1; i >= 0; i--) {
			if (value[i] > o.value[i]) {
				return Boolean.TRUE;
			}
			if (o.value[i] > value[i]) {
				return Boolean.FALSE;
			}
		}
		return Boolean.FALSE;
	}

	public Boolean lessThan(BigInteger o) {
		if (value.length < o.value.length) {
			return Boolean.TRUE;
		}
		if (o.value.length < value.length) {
			return Boolean.FALSE;
		}
		for (int i = value.length - 1; i >= 0; i--) {
			if (value[i] < o.value[i]) {
				return Boolean.TRUE;
			}
			if (o.value[i] < value[i]) {
				return Boolean.FALSE;
			}
		}
		return Boolean.FALSE;
	}

	public Number multiply(Number o) {
		if (o instanceof BigInteger){
			return multiply((BigInteger) o);
		}
		throw new RuntimeException("Cannot add Number of type " + o.getClass());
	}

	public Number multiply(BigInteger o) {
		int i = 0;
		Number out = this;
		while (new Integer(++i).lessThan(o) == Boolean.TRUE){
			out = out.add(this);
		}
		return out;
	}

	public Number divide(Number o) {
		int i = 0;
		Number out = this;
		if (o.equalTo(ZERO) == Boolean.TRUE){
			if (this.equalTo(ZERO) == Boolean.TRUE){
				return ZERO;
			}
			return o.isNegative() == Boolean.TRUE ? Infinity.NEGATIVE_INFINITY : Infinity.POSITIVE_INFINITY;
		}
		while (new Integer(++i).lessThan(o) == Boolean.TRUE){
			out = out.subtract(this);
		}
		if (out.equalTo(ZERO) == Boolean.TRUE){
			return new Integer(i);
		}
		return new Fraction(this, o);
	}

	// TODO: replace with formatters
//	public java.lang.String binaryString() {
//		return stringInBase(2);
//	}
//
//	public java.lang.String decimalString() {
//		return stringInBase(10);
//	}
//
//	public java.lang.String stringInBase(int base) {
//		long out = 0;
//		for (int i = 0; i < value.length; i++) {
//			out += value[i] * Math.pow(MAX_BYTE_SIZE, i);
//		}
//		StringBuilder sb = new StringBuilder();
//		while (out > 0) {
//			sb.append(out % base);
//			out = out / base;
//		}
//		return sb.reverse().toString();
//	}

	//TODO: Remove java methods
//	public boolean equalTo(Object o) {
//		if (o instanceof Number) {
//			return equalTo((Number) o) == Boolean.TRUE;
//		}
//		return false;
//	}
//
//	public java.lang.String toString() {
//		return decimalString();
//	}
}
