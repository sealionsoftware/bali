package bali;

public interface Quantified<T extends Value> extends Value<T> {

	Boolean greaterThan(T operand);

	Boolean lessThan(T operand);

}
