package bali;

/**
 * User: Richard
 * Date: 05/07/13
 */
public interface Quantified<T extends Value> extends Value<T> {

	@Operator(">")
	public Boolean greaterThan(T o);

	@Operator("<")
	public Boolean lessThan(T o);


}
