package bali;

/**
 * User: Richard
 * Date: 10/06/13
 */
public class Fraction implements Number {

	final Number numerator;
	final Number divisor;

	public Fraction(Number numerator, Number divisor) {
		this.numerator = numerator;
		this.divisor = divisor;
	}

	public Number add(Number o) {
		return null;
	}

	public Number subtract(Number o) {
		return null;
	}

	public Boolean isPositive() {
		return numerator.isPositive();
	}

	public Boolean isNegative() {
		return numerator.isNegative();
	}

	public Boolean isZero() {
		return numerator.isNegative();
	}

	public Number magnitude() {
		return isNegative() == Boolean.TRUE ? inverse() : this;
	}

	public Number inverse() {
		return new Fraction(numerator.inverse(), divisor);
	}

	public Boolean greaterThan(Number o) {
		return null;
	}

	public Boolean greaterThan(BigInteger integer) {


		return null;
	}

	public Boolean lessThan(Number o) {
		return null;
	}

	public Boolean equalTo(Number operand) {
		return null;
	}

	public Number multiply(Number o) {
		return null;
	}

	public Number divide(Number o) {
		return null;
	}
}
