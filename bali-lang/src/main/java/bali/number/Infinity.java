package bali.number;

import bali.Boolean;
import bali.Number;
import bali.annotation.MetaType;
import bali.annotation.MetaTypes;

import static bali.IdentityBoolean.FALSE;
import static bali.IdentityBoolean.TRUE;

/**
 * User: Richard
 * Date: 10/06/13
 */
@MetaType(MetaTypes.CLASS)
public enum Infinity implements Number {

	POSITIVE_INFINITY, NEGATIVE_INFINITY;

	public Boolean isPositive() {
		return this == POSITIVE_INFINITY ? TRUE : FALSE;
	}

	public Boolean isNegative() {
		return this == NEGATIVE_INFINITY ? TRUE : FALSE;
	}

	public Boolean isZero() {
		return FALSE;
	}

	public Number magnitude() {
		return POSITIVE_INFINITY;
	}

	public Number negative() {
		return this == POSITIVE_INFINITY ? NEGATIVE_INFINITY : POSITIVE_INFINITY;
	}

	public Boolean greaterThan(Number o) {
		return this == POSITIVE_INFINITY ? TRUE : FALSE;
	}

	public Boolean lessThan(Number o) {
		return this == NEGATIVE_INFINITY ? TRUE : FALSE;
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
		return this == operand ? TRUE : FALSE;
	}
}
