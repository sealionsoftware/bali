package bali;

public interface Number extends Quantified<Number> {

	Boolean isPositive();

	Boolean isNegative();

	Boolean isZero();

	Number magnitude();

	Number negative();

	Number add(Number o);

	Number subtract(Number o);

	Number multiply(Number o);

	Number divide(Number o);

}
