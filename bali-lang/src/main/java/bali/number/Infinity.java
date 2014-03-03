package bali.number;

import bali.Boolean;
import bali.False;
import bali.Number;
import bali.annotation.Name;

import static bali.Primitive.convert;

/**
 * TODO: shouldnt this be a double implementation?
 * User: Richard
 * Date: 10/06/13
 */
public enum Infinity implements Number {

	POSITIVE_INFINITY, NEGATIVE_INFINITY;

	public Boolean isPositive() {
		return convert(this == POSITIVE_INFINITY);
	}

	public Boolean isNegative() {
		return convert(this == NEGATIVE_INFINITY);
	}

	public Boolean isZero() {
		return False.FALSE;
	}

	public Number magnitude() {
		return POSITIVE_INFINITY;
	}

	public Number negative() {
		return this == POSITIVE_INFINITY ? NEGATIVE_INFINITY : POSITIVE_INFINITY;
	}

	public Boolean greaterThan(Number o) {
		return convert(this == POSITIVE_INFINITY);
	}

	public Boolean lessThan(Number o) {
		return convert(this == NEGATIVE_INFINITY);
	}

	public Number add(Number o) {
		return this;
	}

	public Number subtract(Number o) {
		return this;
	}

	public Number multiply(Number o) {
		return this;
	}

	public Number divide(Number o) {
		return this;
	}

	public Boolean equalTo(Number operand) {
		return convert(this == operand);
	}

	public Boolean notEqualTo(@Name("operand") Number operand) {
		return equalTo(operand).not();
	}


}
