package bali;

public interface Quantified<T extends Value> extends Value<T> {

	Boolean greaterThan(T o);

	Boolean lessThan(T o);


}
