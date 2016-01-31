package bali;

import bali.annotation.Operator;

public interface Number extends Quantified<Number> {

	Logic isPositive();

	Logic isNegative();

	Logic isZero();

    @Operator("|")
	Number magnitude();

    @Operator("-")
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
