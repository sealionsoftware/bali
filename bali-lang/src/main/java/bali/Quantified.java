package bali;

import bali.annotation.Operator;

public interface Quantified<T extends Value> extends Value<T> {

    @Operator(">")
    Logic greaterThan(T operand);

    @Operator("<")
    Logic lessThan(T operand);

}
