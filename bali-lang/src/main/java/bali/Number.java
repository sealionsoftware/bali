package bali;

public interface Number extends Quantified<Number> {

	Boolean isPositive();

	Boolean isNegative();

	Boolean isZero();

	Number magnitude();

	Number negate();

	Number add(Number operand);

	Number subtract(Number operand);

	Number multiply(Number operand);

	Number divide(Number operand);

}
