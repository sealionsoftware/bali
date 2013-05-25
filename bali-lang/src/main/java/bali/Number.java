package bali;

import java.lang.*;
import java.util.Arrays;

/**
 * Class representing the integer numbers
 *
 * User: Richard
 * Date: 04/05/13
 */
public class Number {

	private static final short MAX_BYTE_SIZE = 0b10000000;
	private static final Number ZERO = new Number(new byte[]{0});

	private final byte[] value;

	Number(byte[] value) {
		this.value = value;
	}

	public Number(int value) {
		byte[] computedValue = new byte[1+ (value / MAX_BYTE_SIZE)];

		int rem = value;
		int i = 0;
		while (rem >= MAX_BYTE_SIZE){
			computedValue[i++] = (byte) (rem % MAX_BYTE_SIZE);
			rem /= MAX_BYTE_SIZE;
		}
		computedValue[i] = (byte) rem;
		this.value = compact(computedValue);
	}

	int toInt(){
		int ret = 0;
		for (int i = 0 ; i < value.length ; i++){
			ret += value[i] * Math.pow(MAX_BYTE_SIZE, i);
		}
		return ret;
	}

	public Number add(Number o) {

		if (o.isZero().value){
			return this;
		}
		if (isZero().value){
			return o;
		}

		if (isPositive().and(o.isNegative()).value){
			return this.subtract(o.inverse());
		} else if (o.isPositive().and(isNegative()).value){
			return o.subtract(this.inverse());
		}

		int length;
		byte[] one;
		byte[] two;

		if (value.length == o.value.length ){
			length = value.length;
			one = value;
			two = o.value;
		} else if (value.length > o.value.length){
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

		for (int i = 0; i < length ; i++){
			int sum = one[i] + two[i] + carry;
			if(sum == 0){
				computedValue[i] = 0;
				carry = (byte) 0;
			} else {
				computedValue[i] = (byte) (sum % MAX_BYTE_SIZE);
				carry = (byte) (sum / MAX_BYTE_SIZE);
			}
		}

		if(carry != 0){
			computedValue = Arrays.copyOf(computedValue, computedValue.length + 1);
			computedValue[computedValue.length - 1] = carry;
		} else {
			computedValue = compact(computedValue);
		}

		return new Number(computedValue);
	}

	private byte[] compact(byte[] array){
		int lastIndex = array.length - 1;
		if (array[lastIndex] == 0){
			while(lastIndex > 0 && array[lastIndex] == 0){
				lastIndex--;
			}
			return Arrays.copyOf(array, lastIndex + 1);
		}
		return array;
	}

	public Number subtract(Number o) {

		if (o.isZero().value){
			return this;
		}
		if (isZero().value){
			return o;
		}
		if (isNegative().and(o.isPositive()).value || isPositive().and(o.isNegative()).value){
			return this.add(o.inverse());
		}
		if (equals(o).value){
			return ZERO;
		}

		boolean flip;
		int length;
		byte[] one;
		byte[] two;

		if (magnitude().greaterThan(o.magnitude()).value){
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

		if (isNegative().and(o.isNegative()).value){
			flip = !flip;
			invert(one);
			invert(two);
		}

		byte[] computedValue = new byte[length];

		for (int i = 0 ; i < length ; i++){
			byte diff = (byte) (one[i] - two[i]);
			if (diff < 0){
				int j = i;
				while(true){
					if (one[j] == 0){
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

		if (flip){
			invert(computedValue);
		}

		return new Number(computedValue);
	}

	public Boolean isPositive() {
		return Boolean.forPrimitive(value[value.length -1] > 0);
	}

	public Boolean isNegative() {
		return Boolean.forPrimitive(value[value.length -1] < 0);
	}

	public Boolean isZero() {
		return Boolean.forPrimitive(value.length == 1 && value[0] == 0);
	}

	public Number magnitude() {
		return isPositive().value ? this : inverse();
	}

	public Number inverse() {
		byte[] ret = Arrays.copyOf(value, value.length);
		invert(ret);
		return new Number(ret);
	}

	private void invert(byte[] in) {
		for (int i = 0 ; i < in.length ; i++){
			in[i] = (byte) -in[i];
		}
	}

	public Boolean equals(Number o) {
		return Boolean.forPrimitive(Arrays.equals(o.value, value));
	}

	public Boolean greaterThan(Number o) {
		if (value.length > o.value.length){
			return Boolean.TRUE;
		}
		if (o.value.length > value.length){
			return Boolean.FALSE;
		}
		for (int i = value.length -1 ; i >= 0 ; i--){
			if (value[i] > o.value[i]){
				return Boolean.TRUE;
			}
			if (o.value[i] < value[i]){
				return Boolean.FALSE;
			}
		}
		return Boolean.FALSE;
	}

	// TODO: replace with formatters
	public java.lang.String binaryString(){
		return stringInBase(2);
	}

	public java.lang.String decimalString(){
		return stringInBase(10);
	}

	public java.lang.String stringInBase(int base){
		long out =0;
		for (int i = 0 ; i < value.length ; i++){
			out += value[i] * Math.pow(MAX_BYTE_SIZE, i);
		}
		StringBuilder sb = new StringBuilder();
		while (out > 0){
			sb.append(out % base);
			out = out / base;
		}
		return sb.reverse().toString();
	}

	//TODO: Remove java methods
	public boolean equals(Object o){
		if (o instanceof Number){
			return equals((Number) o) == Boolean.TRUE;
		}
		return false;
	}

	public java.lang.String toString(){
		return decimalString();
	}
}
