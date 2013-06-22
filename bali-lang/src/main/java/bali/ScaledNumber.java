package bali;

/**
 * User: Richard
 * Date: 11/06/13
 */
public class ScaledNumber implements Number {

	private BigInteger number;
	private BigInteger scalePlace;
	private BigInteger scalePower;

	ScaledNumber(BigInteger number, BigInteger scalePlace, BigInteger scaleMagnitude) {
		this.number = number;
		this.scalePlace = scalePlace;
		this.scalePower = scaleMagnitude;
	}

	public Boolean isPositive() {
		return number.isPositive();
	}

	public Boolean isNegative() {
		return number.isNegative();
	}

	public Boolean isZero() {
		return number.isZero();
	}

	public Number magnitude() {
		return isNegative() == Boolean.TRUE ? negative() : this;
	}

	public Number negative() {
		return new ScaledNumber(number.negative(), scalePlace, scalePower);
	}

	public Boolean greaterThan(Number o) {

		if (o instanceof ScaledNumber) {


		}

		return null;
	}

	public Boolean lessThan(Number o) {
		return null;
	}

	public Number add(Number o) {
		return null;
	}

	public Number subtract(Number o) {
		return null;
	}

	public Number multiply(Number o) {
		return null;
	}

	public Number divide(Number o) {
		return null;
	}

	public Boolean equalTo(Number operand) {
		return null;
	}
}
