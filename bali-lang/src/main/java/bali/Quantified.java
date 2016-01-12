package bali;

import bali.annotation.Operator;

public interface Quantified<T extends Value> extends Value<T> {

    @Operator(">")
	Boolean greaterThan(T operand);

    @Operator("<")
	Boolean lessThan(T operand);

}
