package bali;

import bali.annotation.Operator;

public interface Number extends Quantified<Number> {

	Boolean isPositive();

	Boolean isNegative();

	Boolean isZero();

	Number magnitude();

	Number negate();

    @Operator("+")
	Number add(Number operand);

    @Operator("-")
	Number subtract(Number operand);

    @Operator("*")
	Number multiply(Number operand);

    @Operator("/")
	Number divide(Number operand);

}
